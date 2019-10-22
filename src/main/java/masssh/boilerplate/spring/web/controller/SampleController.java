package masssh.boilerplate.spring.web.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.web.dao.SampleDao;
import masssh.boilerplate.spring.web.dao.elasticsearch.FooRepository;
import masssh.boilerplate.spring.web.model.document.FooDocument;
import masssh.boilerplate.spring.web.model.request.SampleRequest;
import masssh.boilerplate.spring.web.model.response.SuccessResponse;
import masssh.boilerplate.spring.web.model.row.SampleRow;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final SampleDao sampleDao;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final FooRepository fooRepository;

    @GetMapping("/sample1")
    public ResponseEntity<SuccessResponse> sample1() {
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

        return ResponseEntity.ok(new SuccessResponse());
    }

    @PostMapping("/sample2")
    public ResponseEntity<SuccessResponse> sample2(
            @Valid SampleRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(BAD_REQUEST, bindingResult.toString());
        }
        return ResponseEntity.ok(new SuccessResponse());
    }

    @SuppressWarnings("ConstantConditions")
    @GetMapping("/sample3")
    public String sample3() {
        final boolean flag = true;
        try {
            if (flag) {
                throw new Exception();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom Message Here");
        }
        return "{}";
    }
}
