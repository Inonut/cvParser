package cv.ner;

import cv.support.Util;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by dragos on 31.12.2016.
 */
public class NerClassifierTest {

    private String propertiesPathName = Util.resourcePath + "/train_config.properties";
    private String trainPathName = Util.resourcePath + "/data/trainRetail.txt";
    private String testPathName = Util.resourcePath + "/data/testCV5.txt";

    @Test
    public void classify() throws IOException {

        NerClassifier nerClassifier = new NerClassifier(new FileReader(new File(propertiesPathName)));
        String preparedData = nerClassifier.prepareData(new String(Files.readAllBytes(Paths.get(trainPathName))));
        nerClassifier.train(preparedData);

        nerClassifier.classify(new String(Files.readAllBytes(Paths.get(testPathName))))
                .forEach(pair -> System.out.printf("--------------\n%s: %s\n", pair.getKey(), pair.getValue()));
    }


}