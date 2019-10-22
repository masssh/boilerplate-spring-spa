package masssh.boilerplate.spring.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.web.dao.SampleDao;
import masssh.boilerplate.spring.web.dao.elasticsearch.FooRepository;
import masssh.boilerplate.spring.web.model.document.FooDocument;
import masssh.boilerplate.spring.web.model.row.SampleRow;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final SampleDao sampleDao;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final FooRepository fooRepository;

    @GetMapping("/sample")
    public String sample() {
        // mysql dao access
        final SampleRow row = sampleDao.singleSample(1).orElse(null);
        log.info("dao: {}", row);

        // redis access
        redisTemplate.opsForValue().set("test", 1);
        final Integer val = redisTemplate.opsForValue().get("test");
        log.info("redis: {}", val);

        // elasticsearch repository access
        fooRepository.save(new FooDocument(1));
        final FooDocument fooDocument = fooRepository.findById(1L).orElse(null);
        log.info("elasticsearch: {}", fooDocument);

        return "{}";
    }
}
