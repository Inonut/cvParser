package cv.support;

import cv.support.section.Section;

/**
 * Created by Dragos on 12/16/2016.
 */
public class StringWrapper {

    private String string;
    private Section section;
    private int begin;
    private int end;

    public StringWrapper(String str) {
        this.string = str;
    }

    public StringWrapper replaceAll(String str, Section section, boolean hasEnd){
        if(str == null || section == null){
            return this;
        }
        string = string.replaceAll(str, section.toString() + "\n$0" + (hasEnd ? "\nO": ""));
        return this;
    }

    public StringWrapper replaceAllWord(String str, Section section, boolean hasEnd){
        if(str == null || section == null){
            return this;
        }
        return this.replaceAll(Util.convertToRegex(str), section, hasEnd);
    }

    public StringWrapper replaceAllWord(String str, Section section){
        return this.replaceAllWord(str, section, true);
    }

    @Override
    public String toString() {
        return string;
    }

}

