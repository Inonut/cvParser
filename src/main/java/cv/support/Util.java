package cv.support;


import cv.support.section.Section;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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

    public static List<String> tokens = Arrays.stream(Section.values()).map(Enum::toString).collect(Collectors.toList());

    public static String[] separators = {"â€“", "-" /*45*/, "–" /*150*/};

    public static String convertToRegex(String str){
        return "(?iu)" + Pattern.quote(str);
    }

    public static String normalizeString(String str){
        /*return new String(str)
                .replace('–', '-')
                .replace('©', ' ')
                .replace('’', '\'')
                .replace('‘', '\'');*/

        /*String retValue = "";
        String convertValue2 = "";
        ByteBuffer convertedBytes = null;
        try {

            CharsetEncoder encoder2 = Charset.forName("Windows-1252").newEncoder();
            CharsetEncoder encoder3 = Charset.forName("UTF-8").newEncoder();
            System.out.println("value = " + str);

            assert encoder2.canEncode(str);
            assert encoder3.canEncode(str);

            ByteBuffer conv1Bytes = encoder2.encode(CharBuffer.wrap(str.toCharArray()));

            retValue = new String(conv1Bytes.array(), Charset.forName("Windows-1252"));

            System.out.println("retValue = " + retValue);

            convertedBytes = encoder3.encode(CharBuffer.wrap(retValue.toCharArray()));
            convertValue2 = new String(convertedBytes.array(), Charset.forName("UTF-8"));
            return convertValue2;

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return str;
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
}
