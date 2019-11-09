package masssh.boilerplate.spring.spa.web;

import masssh.boilerplate.spring.spa.web.service.jmx.JMXConnectorServerStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.io.IOException;

@SpringBootApplication
@ComponentScan("masssh.boilerplate.spring.spa.web")
public class SpringWebApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringWebApplication.class, args);
        JMXConnectorServerStarter.startJMXConnectorServer();
    }

}
