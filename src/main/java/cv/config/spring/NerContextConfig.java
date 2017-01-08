package cv.config.spring;

import cv.nlp.service.NlpService;
import cv.ner.NerClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dragos on 12/8/2016.
 */
@Configuration
public class NerContextConfig {

    private NerClassifier nerBulkClassifierCv;
    private NerClassifier nerRetailClassifierCv;

    @Autowired
    private NlpService nlpService;

    @Resource(name="trainProperties")
    private Properties trainProperties;

    public NerContextConfig() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        executor.schedule((Runnable) this::trainNerBulkClassifierCv, 30, TimeUnit.MINUTES);
        executor.schedule((Runnable) this::trainNerRetailClassifierCv, 30, TimeUnit.MINUTES);
    }

    @PostConstruct
    private void trainNerRetailClassifierCv() {
        nerRetailClassifierCv = initTrain(nlpService.processTrainNerRetailData());
//        nerRetailClassifierCv = initTrain(new File("remove/trainRetail.txt"));
    }

    @PostConstruct
    private void trainNerBulkClassifierCv() {
        nerBulkClassifierCv = initTrain(nlpService.processTrainNerBulkData());
//        nerBulkClassifierCv = initTrain(new File("remove/trainBulk.txt"));
    }

    private NerClassifier initTrain(File file) {
        String data= "";
        try {
            data = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return initTrain(data);
    }

    private NerClassifier initTrain(String data) {
        NerClassifier nerClassifier = new NerClassifier(trainProperties);
        String preparedData = nerClassifier.prepareData(data);
        nerClassifier.train(preparedData);

        return nerClassifier;
    }

    @Bean
    public NerClassifier nerBulkClassifierCvService() {
        return nerBulkClassifierCv;
    }

    @Bean
    public NerClassifier nerRetailClassifierCvService() {
        return nerRetailClassifierCv;
    }


    @Bean
    public PropertiesFactoryBean trainProperties() {
        PropertiesFactoryBean properties = new PropertiesFactoryBean();
        properties.setLocation(new ClassPathResource("train_config.properties"));
        return properties;
    }
}
