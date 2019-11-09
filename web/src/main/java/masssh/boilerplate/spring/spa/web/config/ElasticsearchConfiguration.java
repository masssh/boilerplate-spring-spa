package masssh.boilerplate.spring.spa.web.config;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import masssh.boilerplate.spring.spa.property.ApplicationProperty.ElasticsearchProperty;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "masssh.boilerplate.spring.spa.dao.elasticsearch")
@RequiredArgsConstructor
@SuppressWarnings("ALL")
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {
    private final ApplicationProperty applicationProperty;

    @Bean
    public Client elasticsearchClient() throws UnknownHostException {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        final ElasticsearchProperty property = applicationProperty.getElasticsearch();
        Settings settings = Settings.builder().put("cluster.name", property.getClusterName()).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(property.getHost()), property.getPort()));
        return client;
    }

    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(elasticsearchClient(), entityMapper());
    }

    @Bean
    @Override
    public EntityMapper entityMapper() {
        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(
                elasticsearchMappingContext(),
                new DefaultConversionService());
        entityMapper.setConversions(elasticsearchCustomConversions());
        return entityMapper;
    }
}
