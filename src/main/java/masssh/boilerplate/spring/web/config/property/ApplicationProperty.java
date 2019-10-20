package masssh.boilerplate.spring.web.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config")
@Component
@Data
public class ApplicationProperty {
    private MysqlProperty mysql;
    private RedisProperty redis;

    @Data
    public static class MysqlProperty {
        String jdbcUrl;
        String username;
        String password;
        String poolName;
        int minimumIdle;
        int maximumPoolSize;
        long leakDetectionThreshold;
    }

    @Data
    public static class RedisProperty {
        String host;
        int port;
    }
}
