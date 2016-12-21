package cv.nlp.service.collect.impl;

import cv.domain.neo4j.Education;
import cv.support.FindSimilarSequence;
import cv.support.PeriodePrepare;
import cv.support.StreamWrapper;
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

        StreamWrapper streamData = new StreamWrapper(data);
        FindSimilarSequence sequence = new FindSimilarSequence(streamData.collect(Section.EDUCATION_AND_TRAINING)).find();

        List<Education> result = new ArrayList<>();
        sequence.getResult().forEach(seq -> {
            StreamWrapper seqWrapper = new StreamWrapper(seq);
            PeriodePrepare periodePrepare = new PeriodePrepare();
            periodePrepare.split(seqWrapper.collect(Section.PERIODE));

            Education education = new Education();
            education.setDateStart(periodePrepare.next());
            education.setDateEnd(periodePrepare.next());
            education.setPeriode(seqWrapper.collect(Section.PERIODE));
            education.setAdress(seqWrapper.collect(Section.ADRESS));
            education.setDescription(seqWrapper.collect(Section.DESCRIPTION));
            education.setCertificate(seqWrapper.collect(Section.CERTIFICATE));

            result.add(education);

        });

        return result;
    }
}
