package masssh.boilerplate.spring.spa.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config")
@Component
@Data
public class ApplicationProperty {
    private MysqlProperty mysql;
    private RedisProperty redis;
    private ElasticsearchProperty elasticsearch;
    private OAuth2ClientProperty oauth2Google;
    private CorsProperty cors;
    private UrlProperty url;

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

    @Data
    public static class ElasticsearchProperty {
        String host;
        int port;
        String clusterName;
    }

    @Data
    public static class OAuth2ClientProperty {
        String clientId;
        String clientSecret;
        String redirectUriTemplate;
        String scope;
    }

    @Data
    public static class CorsProperty {
        String origin;
    }

    @Data
    public static class UrlProperty {
        String loginSuccess;
    }
}
