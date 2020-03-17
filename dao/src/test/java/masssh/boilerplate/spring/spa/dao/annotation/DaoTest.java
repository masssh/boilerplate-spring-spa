package masssh.boilerplate.spring.spa.dao.annotation;

import masssh.boilerplate.spring.spa.dao.config.DatabaseConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes = {DatabaseConfiguration.class})
@ComponentScan("masssh.boilerplate.spring.spa")
@EnableConfigurationProperties
@Transactional
public @interface DaoTest {
}
