package cv.config.spring;

import cv.support.Util;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * @author amitrus
 */
@Configuration
@EnableNeo4jRepositories("cv.dao.repositories.neo4j")
@PropertySource("classpath:database.neo4j.properties")
@ComponentScan(basePackages = "cv.**.neo4j")
public class Neo4jContextConfig extends org.springframework.data.neo4j.config.Neo4jConfiguration {
    @Resource
    private Environment env;

    public Neo4jContextConfig() {
        Util.setDefaultCharsetToUnicodeUtf8();
    }

    @Bean(name = "neo4jTransactionManager")
    @Qualifier("neo4jTransactionManager")
    @Override
    public PlatformTransactionManager transactionManager() throws Exception {
        return super.transactionManager();
    }

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {

        String DB_Location = env.getRequiredProperty("database.storage.path");

        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
                .setURI("file://" + DB_Location);
        config.set("file.encoding", "UTF-8");

        config.set("dbms.allow_format_migration", true);
        return config;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory(getConfiguration(), "cv.domain.neo4j");
    }
}
