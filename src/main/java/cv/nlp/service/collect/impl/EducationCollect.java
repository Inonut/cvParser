package cv.nlp.service.collect.impl;

import cv.domain.neo4j.Education;
import cv.support.*;
import cv.nlp.service.collect.Worker;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12/15/2016.
 */
public class EducationCollect implements Worker<List<Education>> {

    @Override
    public List<Education> work(List<Pair<Section, SectionContent>> data) {

        DataWrapper streamData = new DataWrapper(data);
        FindSimilarSequence sequence = new FindSimilarSequence(streamData.collect(Section.EDUCATION_AND_TRAINING)).find();

        List<Education> result = new ArrayList<>();
        sequence.getResult().forEach(seq -> {
            DataWrapper seqWrapper = new DataWrapper(seq);
            StringWrapper stringWrapper = new StringWrapper(seqWrapper.collect(Section.DATE)).split(Util.stringDel);

            Education education = new Education();
            education.setDateStart(stringWrapper.pop());
            education.setDateEnd(stringWrapper.pop());
            education.setAdress(seqWrapper.collect(Section.ADRESS));
            education.setDescription(seqWrapper.collect(Section.DESCRIPTION));
            education.setCertificate(seqWrapper.collect(Section.CERTIFICATE));

            result.add(education);

        });

        return result;
    }
}
