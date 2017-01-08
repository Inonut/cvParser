package cv.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dragos on 02.01.2017.
 */

@Configuration
@ComponentScan(basePackages = "cv.**.extraction")
public class ExtractionContextConfig {
}
