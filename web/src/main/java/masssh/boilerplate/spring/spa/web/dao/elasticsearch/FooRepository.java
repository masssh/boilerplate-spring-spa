package masssh.boilerplate.spring.spa.web.dao.elasticsearch;


import masssh.boilerplate.spring.spa.web.model.document.FooDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FooRepository extends ElasticsearchRepository<FooDocument, Long> {
}
