package masssh.boilerplate.spring.spa.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public class ApplicationProperty {
    @Data
    @Component
    @ConfigurationProperties(prefix = "config.api")
    public static class ApiProperty {
        private String endpoint;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "config.web")
    public static class WebProperty {
        private String host;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "config.mysql")
    public static class MysqlProperty {
        private String jdbcUrl;
        private String username;
        private String password;
        private String poolName;
        private int minimumIdle;
        private int maximumPoolSize;
        private long leakDetectionThreshold;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "config.redis")
    public static class RedisProperty {
        private String host;
        private int port;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "config.elasticsearch")
    public static class ElasticsearchProperty {
        private String host;
        private int port;
        private String clusterName;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "config.oauth2-google")
    public static class OAuth2ClientProperty {
        private String clientId;
        private String clientSecret;
        private String redirectUriTemplate;
        private String scope;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "config.security")
    public static class SecurityProperty {
        private String allowOrigin;
        private String loginSuccess;
    }
}
