package cv.support;

import cv.support.section.Section;
import cv.support.section.SectionContent;
import cv.support.section.impl.StringSection;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Dragos on 12/12/2016.
 */
public class DataWrapper {

    private List<Pair<Section, SectionContent>> data;

    public DataWrapper(List<Pair<Section, SectionContent>> data){
        this.data = data;
    }

    public <T> T collect(Section section) {

        if(section == null){
            return null;
        }

        Pair<Section, SectionContent> pair = data.stream().filter(p -> section.equals(p.getKey())).findFirst().orElse(null);
        if(pair == null){
            return null;
        }

        return pair.getValue().getContent();
    }
}
