package cv.support;

import cv.support.section.Section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dragos on 12/16/2016.
 */
public class StringWrapper {

    private String string;
    private List<String> words;

    public StringWrapper(String str) {
        this.string = str;
        this.words = new ArrayList<>();
    }

    public StringWrapper wrapAllMatchDefaut(String str, Section section){
        return wrapAllMatch(str, section, Section.O);
    }

    public StringWrapper wrapAllMatch(String str, Section section){
        return wrapAllMatch(str, section, null);
    }

    public StringWrapper wrapAllMatch(String str, Section sectionStart, Section sectionEnd){
        if(str == null || "".equals(str) || sectionStart == null && sectionEnd == null){
            return this;
        }

        String strat = sectionStart == null ? "" : sectionStart.toString();
        String end = sectionEnd == null ? "" : sectionEnd.toString();
        string = string.replaceAll(Util.escapeRegex(str), "\n" + strat + "\n$0\n" + end + "\n");

        return this;
    }

    public StringWrapper split(String regex) {
        if(regex != null && string != null){
            this.words = Arrays.stream(string.split(regex)).map(String::trim).filter(s -> !"".equals(s)).collect(Collectors.toList());
        }
        return this;
    }

    public String pop(){
        String date = null;
        if(hasNext()){
            date = words.get(0);
            words.remove(0);
        }
        return date;
    }

    public boolean hasNext(){
        return !words.isEmpty();
    }

    @Override
    public String toString() {
        return string;
    }

}

