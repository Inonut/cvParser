package cv.config.spring;

import cv.support.ApplicationConstants;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

/**
 * @author amitrus
 */
@Configuration
@Import({Neo4jContextConfig.class, ServicesContextConfig.class, NerContextConfig.class, ExtractionContextConfig.class})
public class MainContextConfig {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocations(
                new ClassPathResource("application.properties")
        );

        return propertyPlaceholderConfigurer;
    }

    @Bean(initMethod = "getInstance")
    public ApplicationConstants applicationConstants() {
        return ApplicationConstants.getInstance();
    }
}
