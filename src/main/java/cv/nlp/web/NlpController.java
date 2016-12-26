package cv.nlp.web;

import cv.nlp.service.NlpService;
import cv.support.ApplicationConstants;
import cv.support.Util;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import cv.tika.TikaExtraction;
import cv.tika.TikaExtraction2;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
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
//        String inputData = Util.normalizeString(new String(Files.readAllBytes(Paths.get("D:\\KEPLER-PROJECTS\\Modules\\LOCAL\\nertrain\\testCV4.txt"))));
        String inputData = Util.normalizeString(new TikaExtraction2().perform(new File("test/linux.txt")));
        List<Pair<Section, SectionContent>> data = nlpService.classify(inputData);

        nlpService.saveTrain(data, inputData);
    }

    @RequestMapping("/clear")
    public void clear() throws IOException {
        nlpService.clear();
    }
}