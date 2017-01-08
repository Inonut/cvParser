package cv.nlp.service;

import cv.domain.neo4j.DomainObject;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.io.File;
import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */
public interface NlpService {

    void saveTrain(List<Pair<Section, SectionContent>> data, String inputData);

    List<Pair<Section, SectionContent>> classify(String inputData);

    String processTrainNerBulkData();

    String processTrainNerRetailData();

    void clear();

    String extractText(File file);

    void save(DomainObject domainObject);
}
