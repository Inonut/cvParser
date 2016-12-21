package cv.config.spring;

import cv.nlp.service.NlpService;
import cv.support.Util;
import cv.train.NerClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dragos on 12/8/2016.
 */
@Configuration
public class NerContextConfig {
    private NerClassifier nerClassifierCv;
    private NerClassifier nerClassifierCompetences;

    @Autowired
    private NlpService nlpService;

    @Resource(name="trainProperties")
    private Properties trainProperties;

    public NerContextConfig() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                initTrain();
            }
        }, 1800000, 1800000);
    }

    @PostConstruct
    private void initTrain() {
//        nerClassifierCv = initTrain(nlpService.processTrainData());
//        nerClassifierCompetences = initTrain(nlpService.processCompetencesTrainData());
//
        nerClassifierCv = initTrain(new File("D:\\KEPLER-PROJECTS\\Modules\\LOCAL\\nertrain\\src\\main\\resources\\remove\\train3.txt"));
        nerClassifierCompetences = initTrain(new File("D:\\KEPLER-PROJECTS\\Modules\\LOCAL\\nertrain\\src\\main\\resources\\remove\\train.txt"));
    }

    private NerClassifier initTrain(File file) {
        try {
            return initTrain(Util.normalizeString(new String(Files.readAllBytes(file.toPath()))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private NerClassifier initTrain(String data) {
        NerClassifier nerClassifier = new NerClassifier(trainProperties);
        String preparedData = nerClassifier.prepareData(data);
        nerClassifier.train(preparedData);

        return nerClassifier;
    }

    @Bean
    public NerClassifier trainCvService() {
        return nerClassifierCv;
    }

    @Bean
    public NerClassifier trainCompetenceService() {
        return nerClassifierCompetences;
    }
/*
    @Bean
    public PropertiesFactoryBean trainProperties() {
        PropertiesFactoryBean properties = new PropertiesFactoryBean();
        properties.setLocation(new ClassPathResource("train_config.properties"));
        return properties;
    }
*/
}
