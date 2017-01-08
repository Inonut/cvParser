package cv.nlp.service.collect.impl;

import cv.domain.neo4j.PersonalInfo;
import cv.support.DataWrapper;
import cv.nlp.service.collect.Worker;
import cv.support.StringWrapper;
import cv.support.Util;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dragos on 12/12/2016.
 */
public class PersonalInfoCollect implements Worker<PersonalInfo> {

    @Override
    public PersonalInfo work(List<Pair<Section, SectionContent>> data) {
        PersonalInfo personalInfo = new PersonalInfo();
        DataWrapper streamData = new DataWrapper(data);

        StringWrapper stringWrapper = new StringWrapper(streamData.collect(Section.PHONE)).split("\n");

        personalInfo.setName(streamData.collect(Section.PERSON));
        personalInfo.setAdress(streamData.collect(Section.ADRESS));
        personalInfo.setEmail(streamData.collect(Section.EMAIL));
        personalInfo.setPhone1(stringWrapper.pop());
        personalInfo.setPhone2(stringWrapper.pop());
        personalInfo.setSex(streamData.collect(Section.SEX));
        personalInfo.setBirthDate(streamData.collect(Section.BERTH_DATE));
        personalInfo.setJobAppliedFor(streamData.collect(Section.JOB_ROLE));

        return personalInfo;
    }
}
