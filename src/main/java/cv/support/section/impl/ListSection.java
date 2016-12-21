package cv.support.section.impl;

import cv.support.section.SectionContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12/15/2016.
 */
public class ListSection implements SectionContent {

    private List content;

    public ListSection(List content) {
        this.content = content;
    }

    @Override
    public ListSection concat(SectionContent other) {
        List newContent = new ArrayList<>();
        newContent.addAll(content);
        newContent.addAll(other.getContent());
        return new ListSection(newContent);
    }

    @Override
    public List getContent() {
        return this.content;
    }
}
