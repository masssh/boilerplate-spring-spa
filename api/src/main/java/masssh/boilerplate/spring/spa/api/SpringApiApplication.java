package masssh.boilerplate.spring.spa.api;

import masssh.boilerplate.spring.spa.api.service.jmx.JMXConnectorServerStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.io.IOException;

@SpringBootApplication
@ComponentScan("masssh.boilerplate.spring.spa")
public class SpringApiApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringApiApplication.class, args);
        JMXConnectorServerStarter.startJMXConnectorServer();
    }

}
