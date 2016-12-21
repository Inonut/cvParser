package cv.support.section.impl;

import cv.support.section.SectionContent;

/**
 * Created by Dragos on 12/15/2016.
 */
public class StringSection implements SectionContent {

    private String content;

    public StringSection(String content) {
        this.content = content;
    }

    @Override
    public StringSection concat(SectionContent other) {
        return new StringSection(content.concat(other.getContent()));
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
