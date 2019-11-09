package masssh.boilerplate.spring.web.annotation;

import masssh.boilerplate.spring.web.config.ApplicationConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = ApplicationConfiguration.class,
        initializers = {ConfigFileApplicationContextInitializer.class})
@TestPropertySource(properties = {"classpath:/config/application.yml", "classpath:/config/application-test.yml"})
@ActiveProfiles(profiles = "test")
public @interface TestSetUp {
}
