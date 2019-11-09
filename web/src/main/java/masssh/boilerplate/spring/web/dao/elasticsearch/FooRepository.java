package masssh.boilerplate.spring.web.dao.elasticsearch;


import masssh.boilerplate.spring.web.model.document.FooDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FooRepository extends ElasticsearchRepository<FooDocument, Long> {
}
