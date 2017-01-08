package cv.support;


import cv.support.section.Section;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dragos on 11/19/16.
 */
public class Util {

    public static String resourcePath = "./src/main/resources";

    public static List<String> tokens = Arrays.stream(Section.values()).map(Enum::toString).collect(Collectors.toList());

    @Deprecated
    public static String[] separators = {"â€“", "-" /*45*/, "–" /*150*/, " ", "\n"};
    public static String stringDel = "\n\n\n\n\n\n\n\n";

    public static String escapeRegex(String str){
        return "(?iu)" + Pattern.quote(str);
    }

    public static Properties loadProps(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static String[] generateRandomWords() {
        Random random = new Random();
        return generateRandomWords(random.nextInt(10) + 1);
    }

    public static String[] generateRandomWords(int numberOfWords) {
        if(numberOfWords < 1){
            return generateRandomWords();
        }

        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++){
            char[] word = new char[random.nextInt(8)+3];
            for(int j = 0; j < word.length; j++){
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    public static String generateText(int numberOfWords){
        return Arrays.stream(generateRandomWords(numberOfWords)).collect(Collectors.joining(" "));
    }

    public static void setDefaultCharsetToUnicodeUtf8(){
        try {
            System.setProperty("file.encoding","UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static String nameWithoutExtension(File file){
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        return name;
    }
}
