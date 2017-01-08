package cv.extraction.impl;

import cv.support.Util;
import cv.extraction.Extraction;
import org.junit.Test;

import java.io.File;

/**
 * Created by dragos on 31.12.2016.
 */
public class TikaAutoExtractionTest {

    private String testPathName = Util.resourcePath + "/data/testCV6.png";

    @Test
    public void perform() throws Exception {

        Extraction extraction = new TikaAutoExtraction();

        String text = extraction.perform(new File(testPathName));


        System.out.println(text);

//        GoogleTranslator
    }

}