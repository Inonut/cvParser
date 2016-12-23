package cv.nlp.service.impl;

import cv.dao.repositories.neo4j.CvRepository;
import cv.domain.neo4j.Cv;
import cv.domain.neo4j.PersonalInfo;
import cv.nlp.service.NlpService;
import cv.nlp.service.collect.CvBuilder;
import cv.nlp.service.collect.impl.EducationCollect;
import cv.nlp.service.collect.impl.JobInfoCollect;
import cv.nlp.service.collect.impl.LanguageCollect;
import cv.nlp.service.collect.impl.PersonalInfoCollect;
import cv.support.StringWrapper;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import cv.support.section.impl.ListSection;
import cv.train.NerClassifier;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    public void saveTrain(List<Pair<Section, SectionContent>> data, String inputData) {

        Cv cv = new Cv();
        cv.setInputData(inputData);

        CvBuilder cvBuilder = new CvBuilder(data)
                .withWorker(new PersonalInfoCollect()).execute(cv::setPersonalInfo)
                .withWorker(new JobInfoCollect()).execute(cv::setJobInfos)
                .withWorker(new EducationCollect()).execute(cv::setEducations)
                .withWorker(new LanguageCollect()).execute(cv::setLanguages);

        cvRepository.save(cv);
    }

    @Override
    public List<Pair<Section, SectionContent>> classify(String inputData) {
        List<Pair<Section, SectionContent>> cv = nerBulkClassifierCv.classifyAndPrepareResult(inputData);

        cv = CvBuilder.groupData(cv, Section.WORK_EXPERIENCE, Section.EDUCATION_AND_TRAINING).getData();

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
            List inputData = nerRetailClassifierCv.classifyAndPrepareResult(work.getValue().getContent());
            if(grouping){
                List content = CvBuilder.groupData(inputData).getData();
                cv.add(index, new Pair<>(work.getKey(), new ListSection(content)));
            } else {
                cv.add(index, new Pair<>(work.getKey(), new ListSection(inputData)));
            }
        }
    }

    @Override
    public String processTrainData() {

        List<Cv> cvs= cvRepository.loadCvs();

        return cvs.stream().map(cv -> {
            StringWrapper stringWrapper = new StringWrapper(cv.getInputData());

            //personal info
            PersonalInfo personalInfo = cv.getPersonalInfo();
            if(personalInfo != null){
                stringWrapper.replaceAllWord(personalInfo.getName(), Section.PERSON);
                stringWrapper.replaceAllWord(personalInfo.getAdress(), Section.ADRESS);
                stringWrapper.replaceAllWord(personalInfo.getPhone1(), Section.PHONE);
                stringWrapper.replaceAllWord(personalInfo.getPhone2(), Section.PHONE);
                stringWrapper.replaceAllWord(personalInfo.getEmail(), Section.EMAIL);
                stringWrapper.replaceAllWord(personalInfo.getBirthDate(), Section.BERTH_DATE);
            }

            //job info
            if(cv.getJobInfos() != null){
                cv.getJobInfos().forEach(jobInfo -> {
                    stringWrapper.replaceAllWord(jobInfo.getPeriode(), Section.WORK_EXPERIENCE, false);
                    stringWrapper.replaceAllWord(jobInfo.getJobRole(), Section.WORK_EXPERIENCE, false);
                    stringWrapper.replaceAllWord(jobInfo.getEmployer(), Section.WORK_EXPERIENCE, false);
                    stringWrapper.replaceAllWord(jobInfo.getAdress(), Section.WORK_EXPERIENCE, false);
                    stringWrapper.replaceAllWord(jobInfo.getDescription(), Section.WORK_EXPERIENCE, false);
                });
            }


            //education
            if(cv.getEducations() != null){
                cv.getEducations().forEach(education -> {
                    stringWrapper.replaceAllWord(education.getPeriode(), Section.EDUCATION_AND_TRAINING, false);
                    stringWrapper.replaceAllWord(education.getCertificate(), Section.EDUCATION_AND_TRAINING, false);
                    stringWrapper.replaceAllWord(education.getAdress(), Section.EDUCATION_AND_TRAINING, false);
                    stringWrapper.replaceAllWord(education.getDescription(), Section.EDUCATION_AND_TRAINING, false);
                });
            }

            //language
            if(cv.getLanguages() != null){
                cv.getLanguages().forEach(language -> {
                    stringWrapper.replaceAllWord(language.getName(), Section.LANGUAGE, false);
                    stringWrapper.replaceAllWord(language.getLevel(), Section.LANGUAGE, false);
                });
            }

            return Section.O.toString() + "\n" + stringWrapper.toString();

        }).reduce(String::concat).orElse("");
    }

    @Override
    public String processCompetencesTrainData() {
        List<Cv> cvs= cvRepository.loadCvs();

        return cvs.stream().map(cv -> {
            StringWrapper stringWrapper = new StringWrapper(cv.getInputData());

            //personal info
            PersonalInfo personalInfo = cv.getPersonalInfo();
            if(personalInfo != null){
                stringWrapper.replaceAllWord(personalInfo.getName(), Section.PERSON);
                stringWrapper.replaceAllWord(personalInfo.getAdress(), Section.ADRESS);
                stringWrapper.replaceAllWord(personalInfo.getPhone1(), Section.PHONE);
                stringWrapper.replaceAllWord(personalInfo.getPhone2(), Section.PHONE);
                stringWrapper.replaceAllWord(personalInfo.getEmail(), Section.EMAIL);
                stringWrapper.replaceAllWord(personalInfo.getBirthDate(), Section.BERTH_DATE);
            }

            //job info
            if(cv.getJobInfos() != null){
                cv.getJobInfos().forEach(jobInfo -> {
                    stringWrapper.replaceAllWord(jobInfo.getPeriode(), Section.PERIODE);
                    stringWrapper.replaceAllWord(jobInfo.getJobRole(), Section.JOB_ROLE);
                    stringWrapper.replaceAllWord(jobInfo.getEmployer(), Section.ANGAJATOR);
                    stringWrapper.replaceAllWord(jobInfo.getAdress(), Section.ADRESS);
                    stringWrapper.replaceAllWord(jobInfo.getDescription(), Section.DESCRIPTION);
                });
            }


            //education
            if(cv.getEducations() != null){
                cv.getEducations().forEach(education -> {
                    stringWrapper.replaceAllWord(education.getPeriode(), Section.PERIODE);
                    stringWrapper.replaceAllWord(education.getCertificate(), Section.CERTIFICATE);
                    stringWrapper.replaceAllWord(education.getAdress(), Section.ADRESS);
                    stringWrapper.replaceAllWord(education.getDescription(), Section.DESCRIPTION);
                });
            }

            //language
            if(cv.getLanguages() != null){
                cv.getLanguages().forEach(language -> {
                    stringWrapper.replaceAllWord(language.getName(), Section.LANGUAGE_NAME);
                    stringWrapper.replaceAllWord(language.getLevel(), Section.LANGUAGE_LEVEL);
                });
            }

            return Section.O.toString() + "\n" + stringWrapper.toString();

        }).reduce(String::concat).orElse("");
    }

    @Override
    public void clear() {
        cvRepository.deleteAll();
    }
}
