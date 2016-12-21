package cv.nlp.service;

import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */
public interface NlpService {

    void saveTrain(List<Pair<Section, SectionContent>> data, String inputData);

    List<Pair<Section, SectionContent>> classify(String inputData);

    String processTrainData();

    String processCompetencesTrainData();
}
