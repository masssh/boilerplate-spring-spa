package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.api.service.CookieService;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.model.cookie.UserToken;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
public class CookiePreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private final UserService userService;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return Optional.ofNullable(CookieService.findCookie(request.getCookies(), "id"))
                       .map(cookie -> userService.decodeUserToken(cookie.getValue()))
                       .map(UserToken::getUserId)
                       .orElse(null);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return Optional.ofNullable(CookieService.findCookie(request.getCookies(), "id"))
                       .map(cookie -> userService.decodeUserToken(cookie.getValue()))
                       .map(UserToken::getAccessToken)
                       .orElse(null);
    }
}
