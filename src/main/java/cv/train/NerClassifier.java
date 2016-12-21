package cv.train;

import cv.support.section.Section;
import cv.support.Util;
import cv.support.section.SectionContent;
import cv.support.section.impl.StringSection;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dragos on 11/19/16.
 */
public class NerClassifier extends CRFClassifier{

    public NerClassifier() {
    }

    public NerClassifier(Properties props) {
        super(props);
    }

    public NerClassifier(SeqClassifierFlags flags) {
        super(flags);
    }

    public NerClassifier(CRFClassifier crf) {
        super(crf);
    }

    public NerClassifier(Reader reader) {
        this(Util.loadProps(reader));
    }

    public void train(String data){
        this.train(this.makeObjectBankFromString(data, this.defaultReaderAndWriter()));
    }

    private String prepareData(List<Word> words) {
        String result = "";

        String key = "";
        String token = null;
        for (String word: words.stream().map(Word::word).collect(Collectors.toList())){
            if(Util.tokens.contains(word)){
                key = word;
            } else {
                result = result.concat(word.concat(" ").concat(key)).concat("\n");
            }
        }

        return result;
    }

    public String prepareData(File file) throws FileNotFoundException {
        List<Word> words = PTBTokenizer.newPTBTokenizer(new FileReader(file)).tokenize();
        return prepareData(words);
    }

    public String prepareData(String data){
        List<Word> words = PTBTokenizer.newPTBTokenizer(new StringReader(data)).tokenize();
        return prepareData(words);
    }

    public List<Pair<String, String>> classify(File file) {
        String textFile = "";
        try {
            textFile = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return this.classify(textFile);
    }

    public List<Pair<String, String>> classify(String str) {
        List<Pair<String, String>> result = new ArrayList<>();

        String background;
        String tag;
        int start;

        background = this.classifyWithInlineXML(str);

        Set<String> tagPattern = this.labels();
        String startPattern = this.backgroundSymbol();
        StringBuilder endPattern = new StringBuilder();

        for (String m : tagPattern) {
            if (!startPattern.equals(m)) {
                if (endPattern.length() > 0) {
                    endPattern.append('|');
                }

                endPattern.append(m);
            }
        }

        Pattern finalText1 = Pattern.compile("<(" + endPattern + ")>");
        Pattern m = Pattern.compile("</(" + endPattern + ")>");

        for(Matcher color = finalText1.matcher(background); color.find(); color = finalText1.matcher(tag)) {
            int newTag = color.start();
            tag = color.replaceFirst("");
            color = m.matcher(tag);
            if(color.find()) {
                start = color.start();
                String m1 = color.group(1);
                tag = color.replaceFirst("");

//                System.err.println(m1 + ": " + tag.substring(newTag, start));
                result.add(new Pair<>(m1, tag.substring(newTag, start)));
            } else {
                System.err.println("Couldn\'t find end pattern!");
            }
        }

        return result;
    }

    public List<Pair<Section, SectionContent>> classifyAndPrepareResult(String str) {
        return this.classify(str).stream()
                .map(pair -> new Pair<Section, SectionContent>(Section.valueOf(pair.getKey()), new StringSection(pair.getValue())))
                .collect(Collectors.toList());
    }

    @Override
    public void serializeClassifier(String serializePath) {
        File file = new File(serializePath);
        if(!file.exists()){
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.serializeClassifier(serializePath);
    }

    public static void main(String[] args) throws IOException {
        NerClassifier nerClassifier = new NerClassifier(new FileReader(new File("./src/main/resources/train_config.properties")));
        String preparedData = nerClassifier.prepareData(new String(Files.readAllBytes(Paths.get("./src/main/resources/remove/train.txt"))));
        nerClassifier.train(preparedData);

        nerClassifier.classify(new String(Files.readAllBytes(Paths.get("./testCV5P.txt"))))
                .forEach(pair -> System.out.printf("--------------\n%s: %s\n", pair.getKey(), pair.getValue()));

    }

}
