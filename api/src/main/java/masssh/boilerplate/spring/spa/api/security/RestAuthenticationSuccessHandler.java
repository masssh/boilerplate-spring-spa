package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import org.eclipse.jetty.http.HttpHeader;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ApplicationProperty applicationProperty;
    private final UserDao userDao;
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final Object principal = authentication.getPrincipal();
        final UserRow userRow;
        String token;
        if (principal instanceof ApplicationUserDetails) {
            final ApplicationUserDetails user = (ApplicationUserDetails) authentication.getPrincipal();
            userRow = user.getUserRow();
            token = userRow.getAccessToken();
        } else if (principal instanceof DefaultOidcUser) {
            final DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
            final Optional<UserRow> userRowOptional = userDao.singleBySubject(user.getSubject());
            if (userRowOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            userRow = userRowOptional.get();
            token = userRow.getAccessToken();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (StringUtils.isEmpty(token)) {
            token = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
            userRow.setAccessToken(token);
            userDao.update(userRow);
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            setResponse(response, token);
            clearAuthenticationAttributes(request);
            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                    || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            setResponse(response, token);
            clearAuthenticationAttributes(request);
            return;
        }

        setResponse(response, token);
        clearAuthenticationAttributes(request);
    }

    private void setResponse(HttpServletResponse response, String token) throws IOException {
        response.setHeader(HttpHeader.AUTHORIZATION.asString(), String.format("Bearer %s", token));
        response.sendRedirect(applicationProperty.getUrl().getLoginSuccess());
    }
}
