package masssh.boilerplate.spring.spa.dao.elasticsearch;


import masssh.boilerplate.spring.spa.model.document.FooDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FooRepository extends ElasticsearchRepository<FooDocument, Long> {
}
