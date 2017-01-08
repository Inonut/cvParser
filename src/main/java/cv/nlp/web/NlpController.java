package cv.nlp.web;

import cv.domain.neo4j.*;
import cv.nlp.service.NlpService;
import cv.support.Util;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */
@Controller
@RequestMapping("/nlp")
public class NlpController {

    @Autowired
    private NlpService nlpService;

    @RequestMapping("/classify")
    public void classify() throws IOException {
//        String testPath = Util.resourcePath + "/data/testCV5.pdf";
        String testPath = NlpController.class.getResource("/data/testCV4.pdf").getFile();

        String inputData = nlpService.extractText(new File(testPath));

        List<Pair<Section, SectionContent>> data = nlpService.classify(inputData);

        nlpService.saveTrain(data, inputData);
    }

    @RequestMapping("/clear")
    public void clear() throws IOException {
        nlpService.clear();
    }

    //TODO: take data from file
    @RequestMapping("/initDatabase")
    public void initDatabase(){
        nlpService.clear();

        Cv cv = new Cv();

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName("Betty Smith");
        personalInfo.setPhone1("+44 2012345679");
        personalInfo.setPhone2("+44 7123456789");
        personalInfo.setAdress("32 Reading rd, Birmingham, B26 3QJ (United Kingdom)");
        personalInfo.setEmail("smith@kotmail.com");
        personalInfo.setSex("Female");
        personalInfo.setBirthDate("01/03/1975");
        personalInfo.setJobAppliedFor("European project manager");
        cv.setPersonalInfo(personalInfo);

        cv.setJobInfos(new ArrayList<>());

        JobInfo jobInfo = new JobInfo();
        jobInfo.setDateStart("08/2002");
        jobInfo.setDateEnd("Present");
        jobInfo.setJobRole("Independent consultant");
        jobInfo.setEmployer("British Council");
        jobInfo.setAdress("123, Bd Ney, 75023 Paris (France)");
        jobInfo.setDescription("Evaluation of European Commission youth training support measures for youth national\n" +
                "agencies and young people");
        cv.getJobInfos().add(jobInfo);

        jobInfo = new JobInfo();
        jobInfo.setDateStart("03/2002");
        jobInfo.setDateEnd("07/2002");
        jobInfo.setJobRole("Internship");
        jobInfo.setEmployer("European Commission, Youth Unit, DG Education and Culture");
        jobInfo.setAdress("200, Rue de la Loi, 1049 Brussels (Belgium)");
        jobInfo.setDescription("- evaluating youth training programmes for SALTO UK and the partnership between the\n" +
                "Council of Europe and European Commission\n" +
                "- organizing and running a 2 day workshop on non-formal education for Action 5 large scale projects \n" +
                "focusing on quality, assessment and recognition\n" +
                "- contributing to the steering sroup on training and developing action plans on training for\n" +
                "the next 3 years. Working on the Users Guide for training and the support measures\n" +
                "Business or sector European institution");
        cv.getJobInfos().add(jobInfo);

        jobInfo = new JobInfo();
        jobInfo.setDateStart("10/2001");
        jobInfo.setDateEnd("02/2002");
        jobInfo.setJobRole("Researcher / Independent Consultant");
        jobInfo.setEmployer("");
        jobInfo.setAdress("Council of Europe, Budapest (Hungary)");
        jobInfo.setDescription("Working in a research team carrying out in-depth qualitative evaluation of the 2 year Advanced \n" +
                "Training of Trainers in Europe using participant observations, in-depth interviews and focus groups. \n" +
                "Work carried out in training courses in Strasbourg, Slovenia and Budapest.");
        cv.getJobInfos().add(jobInfo);

        cv.setEducations(new ArrayList<>());
        Education education = new Education();
        education.setDateStart("1997");
        education.setDateEnd("2001");
        education.setCertificate("PhD - Thesis Title: 'Young People in the Construction of the Virtual \n" +
                "University’, Empirical research on e-learning");
        education.setAdress("Brunel University, London (United Kingdom)");
        cv.getEducations().add(education);


        education = new Education();
        education.setDateStart("1993");
        education.setDateEnd("1997");
        education.setCertificate("Bachelor of Science in Sociology and Psychology");
        education.setAdress("Brunel University, London (United Kingdom)");
        education.setDescription("- sociology of risk\n" +
                "- sociology of scientific knowledge / information society");
        cv.getEducations().add(education);

        cv.setLanguages(new ArrayList<>());
        Language language = new Language();
        language.setName("English");
        cv.getLanguages().add(language);

        language = new Language();
        language.setName("French");
        language.setLevel("C1 C2 B2 C1 C2");
        cv.getLanguages().add(language);

        language = new Language();
        language.setName("German");
        language.setLevel("A2 A2 A2 A2 A2");
        cv.getLanguages().add(language);

        cv.setInputData(" Curriculum vitae\n" +
                "PERSONAL INFORMATION Betty Smith \n" +
                "32 Reading rd, Birmingham, B26 3QJ (United Kingdom) \n" +
                " +44 2012345679     +44 7123456789    \n" +
                " smith@kotmail.com \n" +
                "AOL Instant Messenger (AIM) betty.smith  \n" +
                "Sex Female | Date of birth 01/03/1975 \n" +
                "JOB APPLIED FOR European project manager\n" +
                "WORK EXPERIENCE\n" +
                "08/2002–Present Independent consultant\n" +
                "British Council\n" +
                "123, Bd Ney, 75023 Paris (France) \n" +
                "Evaluation of European Commission youth training support measures for youth national\n" +
                "agencies and young people\n" +
                "03/2002–07/2002 Internship\n" +
                "European Commission, Youth Unit, DG Education and Culture\n" +
                "200, Rue de la Loi, 1049 Brussels (Belgium) \n" +
                "- evaluating youth training programmes for SALTO UK and the partnership between the\n" +
                "Council of Europe and European Commission\n" +
                "- organizing and running a 2 day workshop on non-formal education for Action 5 large scale projects \n" +
                "focusing on quality, assessment and recognition\n" +
                "- contributing to the steering sroup on training and developing action plans on training for\n" +
                "the next 3 years. Working on the Users Guide for training and the support measures\n" +
                "Business or sector European institution \n" +
                "10/2001–02/2002 Researcher / Independent Consultant\n" +
                "Council of Europe, Budapest (Hungary) \n" +
                "Working in a research team carrying out in-depth qualitative evaluation of the 2 year Advanced \n" +
                "Training of Trainers in Europe using participant observations, in-depth interviews and focus groups. \n" +
                "Work carried out in training courses in Strasbourg, Slovenia and Budapest.\n" +
                "EDUCATION AND TRAINING\n" +
                "1997–2001 PhD - Thesis Title: 'Young People in the Construction of the Virtual \n" +
                "University’, Empirical research on e-learning\n" +
                "Brunel University, London (United Kingdom) \n" +
                "1993–1997 Bachelor of Science in Sociology and Psychology\n" +
                "Brunel University, London (United Kingdom) \n" +
                "- sociology of risk\n" +
                "- sociology of scientific knowledge / information society\n" +
                "13/11/16  © European Union, 2002-2016 | http://europass.cedefop.europa.eu Page 1 / 2\n" +
                "\n" +
                " Curriculum vitae  Betty Smith\n" +
                "- anthropology\n" +
                "E-learning and Psychology\n" +
                "- research methods\n" +
                "PERSONAL SKILLS\n" +
                "Mother tongue(s) English\n" +
                "Other language(s) UNDERSTANDING SPEAKING WRITING\n" +
                "Listening Reading Spoken interaction Spoken production\n" +
                "French C1 C2 B2 C1 C2\n" +
                "German A2 A2 A2 A2 A2\n" +
                "Levels: A1 and A2: Basic user - B1 and B2: Independent user - C1 and C2: Proficient user\n" +
                "Common European Framework of Reference for Languages \n" +
                "Communication skills - team work: I have worked in various types of teams from research teams to national league hockey. \n" +
                "For 2 years I coached my university hockey team\n" +
                "- mediating skills: I work on the borders between young people, youth trainers, youth policy and \n" +
                "researchers, for example running a 3 day workshop at CoE Symposium ‘Youth Actor of Social \n" +
                "Change’, and my continued work on youth training programmes\n" +
                "- intercultural skills: I am experienced at working in a European dimension such as being a rapporteur \n" +
                "at the CoE Budapest ‘youth against violence seminar’ and working with refugees.\n" +
                "Organisational / managerial skills - whilst working for a Brussels based refugee NGO ‘Convivial’ I organized a ‘Civil Dialogue’\n" +
                "between refugees and civil servants at the European Commission 20th June 2002\n" +
                "- during my PhD I organised a seminar series on research methods\n" +
                "Digital competence - competent with most Microsoft Office programmes\n" +
                "- experience with HTML\n" +
                "Other skills Creating pieces of Art and visiting Modern Art galleries. Enjoy all sports particularly hockey, football \n" +
                "and running. Love to travel and experience different cultures.\n" +
                "Driving licence A, B\n" +
                "ADDITIONAL INFORMATION\n" +
                "Publications ‘How to do Observations: Borrowing techniques from the Social Sciences to help Participants do \n" +
                "Observations in Simulation Exercises’ Coyote EU/CoE Partnership Publication, (2002).\n" +
                "13/11/16  © European Union, 2002-2016 | http://europass.cedefop.europa.eu Page 2 / 2\n" +
                "\n");

        nlpService.save(cv);
    }
}