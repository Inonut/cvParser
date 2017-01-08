package cv.nlp.service.impl;

import cv.dao.repositories.neo4j.CvRepository;
import cv.domain.neo4j.Cv;
import cv.domain.neo4j.DomainObject;
import cv.domain.neo4j.PersonalInfo;
import cv.extraction.Extraction;
import cv.nlp.service.NlpService;
import cv.nlp.service.collect.CvBuilder;
import cv.nlp.service.collect.impl.EducationCollect;
import cv.nlp.service.collect.impl.JobInfoCollect;
import cv.nlp.service.collect.impl.LanguageCollect;
import cv.nlp.service.collect.impl.PersonalInfoCollect;
import cv.support.StringWrapper;
import cv.support.Util;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import cv.support.section.impl.ListSection;
import cv.ner.NerClassifier;
import javafx.util.Pair;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */

@Service
@Transactional("neo4jTransactionManager")
public class NlpServiceImpl implements NlpService {

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    @Qualifier("nerBulkClassifierCvService")
    private NerClassifier nerBulkClassifierCv;

    @Autowired
    @Qualifier("nerRetailClassifierCvService")
    private NerClassifier nerRetailClassifierCv;

    @Autowired
    @Qualifier("tikaAutoExtraction")
    private Extraction extraction;

    @Override
    public void saveTrain(List<Pair<Section, SectionContent>> data, String inputData) {

        Cv cv = new Cv();
        cv.setInputData(inputData);

        CvBuilder cvBuilder = new CvBuilder(data)
                .withWorker(new PersonalInfoCollect()).execute(cv::setPersonalInfo)
                .withWorker(new JobInfoCollect()).execute(cv::setJobInfos)
                .withWorker(new EducationCollect()).execute(cv::setEducations)
                .withWorker(new LanguageCollect()).execute(cv::setLanguages);

        //this.save(cv);
    }

    @Override
    public List<Pair<Section, SectionContent>> classify(String inputData) {
        List<Pair<Section, SectionContent>> cv = nerBulkClassifierCv.classifyAndPrepareResult(inputData);

        cv = CvBuilder.groupData(cv, Section.WORK_EXPERIENCE, Section.EDUCATION_AND_TRAINING, Section.LANGUAGE).getData();

        prepareComp(cv, Section.WORK_EXPERIENCE);
        prepareComp(cv, Section.EDUCATION_AND_TRAINING);
        prepareComp(cv, Section.LANGUAGE, false);

        return cv;
    }

    private void prepareComp(List<Pair<Section, SectionContent>> cv, Section section){
        prepareComp(cv, section, true);
    }

    private void prepareComp(List<Pair<Section, SectionContent>> cv, Section section, boolean grouping){
        Pair<Section, SectionContent> work = cv.stream().filter(p -> section.equals(p.getKey())).findFirst().orElse(null);
        if(work != null){
            int index = cv.indexOf(work);
            cv.remove(work);
            List inputData = nerRetailClassifierCv.classifyAndPrepareResult(Util.generateText(10) + "\n" + work.getValue().getContent());
            if(grouping){
                List content = CvBuilder.groupData(inputData).getData();
                cv.add(index, new Pair<>(work.getKey(), new ListSection(content)));
            } else {
                cv.add(index, new Pair<>(work.getKey(), new ListSection(inputData)));
            }
        }
    }

    @Override
    public String processTrainNerBulkData() {

        List<Cv> cvs= cvRepository.loadCvs();

        return cvs.stream().map(cv -> {
            StringWrapper stringWrapper = new StringWrapper(cv.getInputData());

            //personal info
            PersonalInfo personalInfo = cv.getPersonalInfo();
            if(personalInfo != null){
                stringWrapper.wrapAllMatchDefaut(personalInfo.getName(), Section.PERSON);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getSex(), Section.SEX);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getAdress(), Section.ADRESS);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getPhone1(), Section.PHONE);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getPhone2(), Section.PHONE);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getEmail(), Section.EMAIL);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getBirthDate(), Section.BERTH_DATE);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getJobAppliedFor(), Section.JOB_ROLE);
            }

            //job info
            if(cv.getJobInfos() != null){
                cv.getJobInfos().forEach(jobInfo -> {
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getJobRole(), Section.WORK_EXPERIENCE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getEmployer(), Section.WORK_EXPERIENCE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getAdress(), Section.WORK_EXPERIENCE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getDescription(), Section.WORK_EXPERIENCE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getDateStart(), Section.WORK_EXPERIENCE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getDateEnd(), Section.WORK_EXPERIENCE);
                });
            }


            //education
            if(cv.getEducations() != null){
                cv.getEducations().forEach(education -> {
                    stringWrapper.wrapAllMatchDefaut(education.getCertificate(), Section.EDUCATION_AND_TRAINING);
                    stringWrapper.wrapAllMatchDefaut(education.getAdress(), Section.EDUCATION_AND_TRAINING);
                    stringWrapper.wrapAllMatchDefaut(education.getDescription(), Section.EDUCATION_AND_TRAINING);
                    stringWrapper.wrapAllMatchDefaut(education.getDateStart(), Section.EDUCATION_AND_TRAINING);
                    stringWrapper.wrapAllMatchDefaut(education.getDateEnd(), Section.EDUCATION_AND_TRAINING);
                });
            }

            //language
            if(cv.getLanguages() != null){
                cv.getLanguages().forEach(language -> {
                    stringWrapper.wrapAllMatchDefaut(language.getName(), Section.LANGUAGE);
                    stringWrapper.wrapAllMatchDefaut(language.getLevel(), Section.LANGUAGE);
                });
            }

            return Section.O.toString() + "\n" + stringWrapper.toString();

        }).reduce(String::concat).orElse("");
    }

    @Override
    public String processTrainNerRetailData() {
        List<Cv> cvs= cvRepository.loadCvs();

        return cvs.stream().map(cv -> {
            StringWrapper stringWrapper = new StringWrapper(cv.getInputData());

            //TODO: is not necessary
            //personal info
            PersonalInfo personalInfo = cv.getPersonalInfo();
            if(personalInfo != null){
                stringWrapper.wrapAllMatchDefaut(personalInfo.getName(), Section.PERSON);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getSex(), Section.SEX);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getAdress(), Section.ADRESS);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getPhone1(), Section.PHONE);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getPhone2(), Section.PHONE);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getEmail(), Section.EMAIL);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getBirthDate(), Section.DATE);
                stringWrapper.wrapAllMatchDefaut(personalInfo.getJobAppliedFor(), Section.JOB_ROLE);
            }

            //job info
            if(cv.getJobInfos() != null){
                cv.getJobInfos().forEach(jobInfo -> {
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getJobRole(), Section.JOB_ROLE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getEmployer(), Section.EMPLOYER);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getAdress(), Section.ADRESS);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getDescription(), Section.DESCRIPTION);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getDateStart(), Section.DATE);
                    stringWrapper.wrapAllMatchDefaut(jobInfo.getDateEnd(), Section.DATE);
                });
            }


            //education
            if(cv.getEducations() != null){
                cv.getEducations().forEach(education -> {
                    stringWrapper.wrapAllMatchDefaut(education.getCertificate(), Section.CERTIFICATE);
                    stringWrapper.wrapAllMatchDefaut(education.getAdress(), Section.ADRESS);
                    stringWrapper.wrapAllMatchDefaut(education.getDescription(), Section.DESCRIPTION);
                    stringWrapper.wrapAllMatchDefaut(education.getDateStart(), Section.DATE);
                    stringWrapper.wrapAllMatchDefaut(education.getDateEnd(), Section.DATE);
                });
            }

            //language
            if(cv.getLanguages() != null){
                cv.getLanguages().forEach(language -> {
                    stringWrapper.wrapAllMatchDefaut(language.getName(), Section.LANGUAGE_NAME);
                    stringWrapper.wrapAllMatchDefaut(language.getLevel(), Section.LANGUAGE_LEVEL);
                });
            }

            return Section.O.toString() + "\n" + stringWrapper.toString();

        }).reduce(String::concat).orElse("");
    }

    @Override
    public void clear() {
        cvRepository.deleteAll();
    }

    @Override
    public String extractText(File file) {
        try {
            return extraction.perform(file) ;
        } catch (IOException | TikaException | SAXException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void save(DomainObject domainObject) {
        cvRepository.save(domainObject);
    }
}
