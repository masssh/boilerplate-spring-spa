package masssh.boilerplate.spring.spa.api.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.security.ApplicationUserDetailsService;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.response.BaseResponse;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserDao userDao;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> addUser(@Valid @RequestBody final LoginRequest request,
                                                 final BindingResult bindingResult,
                                                 @CookieValue(name = "userId")) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }

        final String email = request.getEmail().trim();
        final String password = request.getPassword().trim();

        final Optional<UserRow> userRowOptional = userService.loadUserByEmail(email);
        if (userRowOptional.isEmpty()) {
            user
        }


        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad Credentials");
        }

        // refresh accessToken
        userService.refreshAccessToken(userRow);

        return ResponseEntity.ok(new LoginResponse(OK.value(), "Login successfully", userRow.getAccessToken(), userRow.getRole()));
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
        private final String accessToken;
        private final String role;
    }
}
