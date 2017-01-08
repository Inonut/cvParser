package cv.support.data;

import cv.extraction.Extraction;
import cv.extraction.impl.TikaAutoExtraction;
import cv.support.Util;
import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by dragos on 02.01.2017.
 */
public class ExtractionSupport {

    public static void main(String[] args) throws IOException {

        Path extractPath = Paths.get(Util.resourcePath + "/data-extracted");

        FileUtils.deleteDirectory(extractPath.toFile());
        Files.createDirectory(extractPath);

        Files.walk(Paths.get(Util.resourcePath + "/data"))
                .map(Path::toFile)
                .filter(File::isFile)
                .forEach(file -> {
                    Extraction extraction = new TikaAutoExtraction();
                    String text = "";
                    try {
                        text = extraction.perform(file);
                    } catch (IOException | TikaException | SAXException e) {
                        e.printStackTrace();
                    }

                    try {
                        Path newFile = Files.createFile(extractPath.resolve(Paths.get(file.getName() + ".extract")));
                        Files.write(newFile, text.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("Done!");
    }
}
