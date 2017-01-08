package cv.extraction;

import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Created by dragos on 31.12.2016.
 */

public interface Extraction {

    String perform(File file) throws IOException, TikaException, SAXException;
}
