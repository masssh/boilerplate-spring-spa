package masssh.boilerplate.spring.spa.api.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.security.ApplicationUserDetails;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.model.response.BaseResponse;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/api/token")
    public ResponseEntity<LoginResponse> token(final Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        final Authentication authentication = (Authentication) principal;
        final UserRow userRow;
        if (authentication.getPrincipal() instanceof ApplicationUserDetails) {
            userRow = ((ApplicationUserDetails) authentication.getPrincipal()).getUserRow();
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            final String subject = ((DefaultOidcUser) authentication.getPrincipal()).getSubject();
            userRow = userService.loadUserBySubject(subject).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        } else {
            throw new ResponseStatusException(NOT_FOUND);
        }

        // refresh accessToken
        userService.refreshAccessToken(userRow);

        return ResponseEntity.ok(new LoginResponse(OK.value(),
                "Login successfully",
                userRow.getUserId(),
                userRow.getAccessToken(),
                userRow.getRole()));
    }

    @PostMapping("/api/signIn")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody final LoginRequest loginRequest,
                                                final BindingResult bindingResult) {
        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();
        final UserRow userRow;

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        userRow = userService.loadUserByEmail(email).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        if (!passwordEncoder.matches(password, userRow.getPasswordHash())) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        // refresh accessToken
        userService.refreshAccessToken(userRow);

        return ResponseEntity.ok(new LoginResponse(OK.value(),
                "Login successfully",
                userRow.getUserId(),
                userRow.getAccessToken(),
                userRow.getRole()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class LoginRequest {
        @NotEmpty
        @Email
        private String email;
        @Min(8)
        private String password;
    }

    @Data
    @ToString
    @EqualsAndHashCode(callSuper = true)
    private static final class LoginResponse extends BaseResponse {
        private final int status;
        private final String message;
        private final String userId;
        private final String accessToken;
        private final String role;
    }
}
