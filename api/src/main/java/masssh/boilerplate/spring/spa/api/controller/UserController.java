package masssh.boilerplate.spring.spa.api.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.model.response.SuccessResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/add")
    public ResponseEntity<SuccessResponse> addUser(@Valid @RequestBody final UserAddRequest request,
                                                   final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(BAD_REQUEST, bindingResult.toString());
        }
        userService.registerUser(request.getUserName(),
                request.getPassword(),
                request.getEmail(),
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new SuccessResponse());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserAddRequest {
        @NotEmpty
        private String userName;
        @Min(8)
        private String password;
        @NotEmpty
        @Email
        private String email;
    }
}
