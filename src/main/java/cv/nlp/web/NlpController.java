package cv.nlp.web;

import cv.nlp.service.NlpService;
import cv.support.ApplicationConstants;
import cv.support.Util;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */
@Controller
@RequestMapping("/nlp")
public class NlpController {

    @Autowired
    private NlpService nlpService;

    @Autowired
    private ApplicationConstants applicationConstants;

    @RequestMapping("/classify")
    public void classify() throws IOException {
        String inputData = Util.normalizeString(new String(Files.readAllBytes(Paths.get("D:\\KEPLER-PROJECTS\\Modules\\LOCAL\\nertrain\\testCV4.txt"))));
        List<Pair<Section, SectionContent>> data = nlpService.classify(inputData);

        //data.forEach(pair -> System.out.printf("--------------\n%s: %s\n", pair.getKey(), pair.getValue()));

        nlpService.saveTrain(data, inputData);

      /*  String a = nlpService.processTrainData();
        String b = nlpService.processCompetencesTrainData();
        String c = a+b;*/
    }
}