package cv.nlp.service.collect.impl;

import cv.domain.neo4j.PersonalInfo;
import cv.support.DataWrapper;
import cv.nlp.service.collect.Worker;
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

        String phoneStr = streamData.collect(Section.PHONE);
        List<String> phone = null;
        if(phoneStr != null){
            phone = Arrays.stream(phoneStr.split("\n")).map(String::trim).filter(s -> !"".equals(s)).collect(Collectors.toList());
        }

        personalInfo.setName(streamData.collect(Section.PERSON));
        personalInfo.setAdress(streamData.collect(Section.ADRESS));
        personalInfo.setEmail(streamData.collect(Section.EMAIL));

        if(phone != null){
            personalInfo.setPhone1(phone.get(0));

            if(phone.size() > 1){
                personalInfo.setPhone2(phone.get(1));
            }
        }

        personalInfo.setSex(streamData.collect(Section.SEX));
        personalInfo.setBirthDate(streamData.collect(Section.BERTH_DATE));

        return personalInfo;
    }
}
