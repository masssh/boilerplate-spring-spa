package masssh.boilerplate.spring.spa.web.config;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
@SuppressWarnings("ALL")
public class RedisConfiguration extends ElasticsearchConfigurationSupport {
    private final ApplicationProperty applicationProperty;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        final ApplicationProperty.RedisProperty redisProperty = applicationProperty.getRedis();
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(
                redisProperty.getHost(),
                redisProperty.getPort()
        ));
    }

    @Bean
    public RedisTemplate<String, Integer> intRedisTemplate() {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Long> longRedisTemplate() {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(new LettuceConnectionFactory());
        return redisTemplate;
    }
}
