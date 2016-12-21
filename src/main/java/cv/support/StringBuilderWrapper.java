package cv.support;

import cv.support.section.Section;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Dragos on 12/19/2016.
 */
public class StringBuilderWrapper {

    private StringBuilder stringBuilder;

    private List<String> redundantWords;

    public StringBuilderWrapper() {
        this.stringBuilder = new StringBuilder();
    }

    public StringBuilderWrapper(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public StringBuilderWrapper(List<String> redundantWords) {
        this.stringBuilder = new StringBuilder();
        this.redundantWords = redundantWords;
    }

    public <T> StringBuilderWrapper append(T obj){
        stringBuilder.append(obj == null ? "" : obj).append("\n");
        return this;
    }

    public StringBuilderWrapper appendSection(Section section){
        return appendSection(section, 0);
    }

    public StringBuilderWrapper appendSection(Section section, int words){
        return this.append(Section.O).append(generateWords(words).collect(Collectors.joining(" "))).append(section);
    }

    private Stream<String> generateWords(int words) {

        if(words < 1){
            Random random = new Random();
            return generateWords(random.nextInt(10) + 1);
        }

        if(this.redundantWords != null){
            Collections.shuffle(this.redundantWords);
            return this.redundantWords.subList(0, Math.min(this.redundantWords.size(), words)).stream();
        } else {
            return Arrays.stream(Util.generateRandomWords(words));
        }
    }


    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}