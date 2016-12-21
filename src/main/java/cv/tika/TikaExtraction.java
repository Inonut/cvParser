package cv.tika;

import cv.exception.ExtractionException;
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
 * Created by Dragos on 12/21/2016.
 */
public class TikaExtraction {

    public String perform(File source) throws ExtractionException {

        List<String> chunks = new ArrayList<>();
        ContentHandlerDecorator handler = new ContentHandlerDecorator() {
            @Override
            public void characters(char[] ch, int start, int length) {
                String thisStr = new String(ch, start, length).trim();

                if(!"".equals(thisStr.trim())){
                    chunks.add(thisStr);
                }
            }
        };

        AutoDetectParser parser = new AutoDetectParser();

        Metadata metadata = new Metadata();

        try (InputStream stream = new FileInputStream(source)) {
            parser.parse(stream, handler, metadata);
        } catch (IOException | SAXException | TikaException e) {
            throw new ExtractionException(e);
        }

        return chunks.stream().collect(Collectors.joining("\n"));
    }
}
