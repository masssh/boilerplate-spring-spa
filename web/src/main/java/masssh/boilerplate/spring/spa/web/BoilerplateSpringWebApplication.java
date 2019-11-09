package masssh.boilerplate.spring.spa.web;

import masssh.boilerplate.spring.spa.web.service.jmx.JMXConnectorServerStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class BoilerplateSpringWebApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BoilerplateSpringWebApplication.class, args);
        JMXConnectorServerStarter.startJMXConnectorServer();
    }

}
