package cv.nlp.service.collect.impl;

import cv.domain.neo4j.Language;
import cv.nlp.service.collect.FindSimilarSequence;
import cv.nlp.service.collect.StreamWrapper;
import cv.nlp.service.collect.Worker;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12/15/2016.
 */
public class LanguageCollect implements Worker<List<Language>> {
    @Override
    public List<Language> work(List<Pair<Section, SectionContent>> data) {

        StreamWrapper streamData = new StreamWrapper(data);
        FindSimilarSequence sequence = new FindSimilarSequence(streamData.collect(Section.LANGUAGE)).find();

        List<Language> result = new ArrayList<>();
        sequence.getResult().forEach(seq -> {
            StreamWrapper seqWrapper = new StreamWrapper(seq);

            Language education = new Language();
            education.setName(seqWrapper.collect(Section.LANGUAGE_NAME));
            education.setLevel(seqWrapper.collect(Section.LANGUAGE_LEVEL));

            result.add(education);

        });

        return result;
    }
}
