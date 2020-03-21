package masssh.boilerplate.spring.spa.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.service.MailService;
import masssh.boilerplate.spring.spa.dao.service.UserCreator;
import masssh.boilerplate.spring.spa.model.response.SuccessResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@Profile(value = "local")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LocalOnlyController {
    private final UserCreator userCreator;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @GetMapping("/local/success")
    public ResponseEntity<SuccessResponse> success() {
        return ResponseEntity.ok(new SuccessResponse());
    }

    @GetMapping("/local/not_found")
    public ResponseEntity<Object> notFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/test")
    public ResponseEntity<SuccessResponse> apiTest() {
        return ResponseEntity.ok(new SuccessResponse());
    }

    @GetMapping("/local/encode")
    public ResponseEntity<Map<String, String>> encode(
            @RequestParam("key") final String key
    ) {
        return ResponseEntity.ok(Map.of("hash", passwordEncoder.encode(key)));
    }

    @GetMapping("/local/matches")
    public ResponseEntity<Map<String, Boolean>> decode(
            @RequestParam("key") final String key,
            @RequestParam("encoded") final String encoded
    ) {
        return ResponseEntity.ok(Map.of("hash", passwordEncoder.matches(key, encoded)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserAddRequest {
        private String userId;
        private String password;
        private String name;
        private String email;
    }
}
