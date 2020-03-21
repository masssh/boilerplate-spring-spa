package masssh.boilerplate.spring.spa.api.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.model.response.SuccessResponse;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.property.ApplicationProperty.WebProperty;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final WebProperty webProperty;
    private final UserService userService;

    @GetMapping("/api/token")
    public ResponseEntity<LoginResponse> token(final Principal principal) {
        final UserRow userRow = userService.loadUserByPrincipal(principal)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        return ResponseEntity.ok(new LoginResponse(
                userRow.getUserHash(),
                userRow.getAccessToken(),
                userRow.getRole()));
    }

    @PostMapping("/api/signUp")
    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody final SingUpRequest request,
                                                  final BindingResult bindingResult,
                                                  final Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        if (userService.loadUserByPrincipal(principal).isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }

        try {
            userService.registerUserByEmail(request.getUserName(),
                    request.getPassword(),
                    request.getEmail(),
                    LocaleContextHolder.getLocale());
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new SuccessResponse());
    }

    @GetMapping("/api/user")
    public ResponseEntity<UserResponse> getUser(final Principal principal) {
        final UserRow userRow = userService.loadUserByPrincipal(principal)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        return ResponseEntity.ok(new UserResponse(userRow.getUserName(), userRow.getEmail()));
    }

    @PutMapping("/api/user")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody final UserRequest request,
                                                   final BindingResult bindingResult,
                                                   final Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(BAD_REQUEST, bindingResult.toString());
        }
        final UserRow userRow = userService.loadUserByPrincipal(principal)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        if (!StringUtils.isEmpty(request.getUserName())) {
            userRow.setUserName(request.userName);
        }
        if (!StringUtils.isEmpty(request.getPassword())) {
            userRow.setPasswordHash(userService.generatePasswordHash(request.getPassword()));
        }
        userService.updateUser(userRow);
        return ResponseEntity.ok(new UserResponse(userRow.getUserName(), userRow.getEmail()));
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<SuccessResponse> deleteUser(final Principal principal,
                                                      final HttpServletRequest request) {
        final UserRow userRow = userService.loadUserByPrincipal(principal)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        userService.deleteUser(userRow.getUserHash());
        SecurityContextHolder.clearContext();
        Optional.ofNullable(request.getSession()).ifPresent(HttpSession::invalidate);
        return ResponseEntity.ok(new SuccessResponse());
    }

    @GetMapping("/api/verify/email")
    public RedirectView getUser(@RequestParam("q") final String verificationHash,
                                final HttpServletRequest request) {
        userService.verifyUserByEmail(verificationHash);
        SecurityContextHolder.clearContext();
        Optional.ofNullable(request.getSession()).ifPresent(HttpSession::invalidate);
        return new RedirectView(webProperty.getHost());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    private static final class LoginResponse extends SuccessResponse {
        private String userHash;
        private String accessToken;
        private String role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    private static final class UserResponse extends SuccessResponse {
        private String userName;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SingUpRequest {
        @NotEmpty
        private String userName;
        @Min(8)
        private String password;
        @NotEmpty
        @Email
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserRequest {
        private String userName;
        @Min(8)
        private String password;
    }

}
