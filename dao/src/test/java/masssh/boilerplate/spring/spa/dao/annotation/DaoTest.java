package masssh.boilerplate.spring.spa.dao.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes = DaoTest.DaoTestConfiguration.class)
@EnableConfigurationProperties
@Transactional
public @interface DaoTest {
    @Configuration
    @ComponentScan("masssh.boilerplate.spring.spa")
    @EnableAutoConfiguration
    class DaoTestConfiguration {
    }
}
