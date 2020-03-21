package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
class OpenIdUserService extends OidcUserService {
    private final UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        final OidcUser oidcUser = super.loadUser(userRequest);
        UserRow userRow = null;
        try {
            userRow = userService.registerOidcUser(oidcUser);
        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("Could not register user [subject={}, email={}]", oidcUser.getSubject(), e);
            throw new RuntimeException();
        }
        final OAuth2UserAuthority authority = new OAuth2UserAuthority(userRow.getRole(), oidcUser.getAttributes());
        return new DefaultOidcUser(
                Set.of(authority),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }

}
