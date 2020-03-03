package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.cookie.UserToken;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ApplicationProperty applicationProperty;
    private final UserDao userDao;
    private final UserService userService;
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final Object principal = authentication.getPrincipal();
        final UserRow userRow;
        if (!(principal instanceof DefaultOidcUser)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        final Optional<UserRow> userRowOptional = userDao.singleBySubject(user.getSubject());
        if (userRowOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userRow = userRowOptional.get();
        userService.refreshAccessToken(userRow);

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            setResponse(response, userRow);
            clearAuthenticationAttributes(request);
            return;
        }

        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (
                targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter))
        )) {
            requestCache.removeRequest(request, response);
            setResponse(response, userRow);
            clearAuthenticationAttributes(request);
            return;
        }
        setResponse(response, userRow);
        clearAuthenticationAttributes(request);
    }

    private void setResponse(HttpServletResponse response, UserRow userRow) throws IOException {
        final UserToken token = new UserToken(userRow.getUserId(), userRow.getAccessToken(), userRow.getRole());
        response.sendRedirect(applicationProperty.getSecurity().getLoginSuccess()
                + "?token=" + userService.encodeUserToken(token));
    }
}
