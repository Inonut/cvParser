package cv.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by amitrus on 11/23/16.
 */
@Configuration
@ComponentScan(basePackages = "cv.**.service")
public class ServicesContextConfig {
}
