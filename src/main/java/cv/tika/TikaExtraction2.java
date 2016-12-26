package cv.tika;

import cv.exception.ExtractionException;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dragos on 12/26/2016.
 */
public class TikaExtraction2 {

    public String perform(File source) throws ExtractionException {

//        AutoDetectParser parser = new AutoDetectParser();
//        DefaultDetector detector = new DefaultDetector();

        Metadata metadata = new Metadata();

//        Tika tika = new Tika(detector, parser);
        Tika tika = new Tika();

        try (InputStream stream = new FileInputStream(source)) {
            return tika.parseToString(stream, metadata);
        } catch (IOException | TikaException e) {
            throw new ExtractionException(e);
        }
    }
}
