package cv.nlp.service.collect;

import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Dragos on 12/12/2016.
 */
public interface Worker<R> {
    R work(List<Pair<Section, SectionContent>> data);
}
