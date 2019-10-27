package masssh.boilerplate.spring.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LocalOnlyController {
    private final PasswordEncoder passwordEncoder;

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
}
