package cv.extraction.impl;

import cv.exception.ExtractionException;
import cv.extraction.Extraction;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dragos on 12/26/2016.
 */

@Component
public class TikaAutoExtraction implements Extraction {

    /*Extract from many formates, extract from image if tesseract-ocr is installed and tika version 1.x*/

    @Override
    public String perform(File source) throws ExtractionException {

        Tika tika = new Tika();

        String data = "";
        try (InputStream stream = new FileInputStream(source)) {
            data = tika.parseToString(stream);
        } catch (IOException | TikaException e) {
            throw new ExtractionException(e);
        }

        return data;
    }
}
