package masssh.boilerplate.spring.spa.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.security.Roles;
import masssh.boilerplate.spring.spa.dao.service.UserCreator;
import masssh.boilerplate.spring.spa.model.response.SuccessResponse;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


@Profile(value = "local")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LocalOnlyController {
    private final UserCreator userCreator;
    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/local/user/add")
    public ResponseEntity<Map<String, String>> addUser(final UserAddRequest request) {
        final UserRow userRow = userCreator.tryCreate(
                new UserRow(null,
                        null,
                        request.getName(),
                        Roles.ROLE_USER,
                        request.getEmail(),
                        Locale.JAPAN.toLanguageTag(),
                        passwordEncoder.encode(request.getPassword()),
                        DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                        null));
        return ResponseEntity.ok(Map.of("token", userRow.getAccessToken()));
    }

    @GetMapping("/local/user/add/random")
    public ResponseEntity<Map<String, String>> addUserRandom() {
        final String uid = String.valueOf(Instant.now().getEpochSecond());
        final UserRow userRow = userCreator.tryCreate(
                new UserRow(null,
                        null,
                        "user" + uid,
                        Roles.ROLE_USER,
                        uid + "@example.com",
                        Locale.JAPAN.toLanguageTag(),
                        passwordEncoder.encode("12345678"),
                        DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                        null));
        return ResponseEntity.ok(Map.of("token", userRow.getAccessToken()));
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
