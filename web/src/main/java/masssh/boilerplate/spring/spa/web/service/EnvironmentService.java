package masssh.boilerplate.spring.spa.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class EnvironmentService {
    private static final String LOCAL = "local";
    private final Environment environment;

    public boolean isLocal() {
        return Arrays.asList(environment.getActiveProfiles()).contains(LOCAL);
    }
}
