package masssh.boilerplate.spring.spa.api.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.service.CookieService;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.model.response.BaseResponse;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;
    private final CookieService cookieService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(final HttpServletRequest request,
                                               @Valid @RequestBody final LoginRequest loginRequest,
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
