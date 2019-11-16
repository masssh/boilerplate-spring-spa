package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.OAuth2GoogleDao;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.dao.service.UserCreator;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class OpenIdUserService extends OidcUserService {
    private final UserDao userDao;
    private final UserCreator userCreator;
    private final OAuth2GoogleDao oAuth2GoogleDao;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        final OidcUser oidcUser = super.loadUser(userRequest);
        Optional<UserRow> userRowOptional = userDao.singleBySubject(oidcUser.getSubject());
        UserRow userRow;
        if (userRowOptional.isEmpty()) {
            final OAuth2GoogleRow newOauth2GoogleRow = new OAuth2GoogleRow(
                    oidcUser.getSubject(),
                    oidcUser.getIdToken().getTokenValue(),
                    oidcUser.getAccessTokenHash(),
                    oidcUser.getIssuedAt().getEpochSecond(),
                    oidcUser.getExpiresAt().getEpochSecond());
            oAuth2GoogleDao.create(newOauth2GoogleRow);

            userRow = userCreator.tryCreate(
                    new UserRow(newOauth2GoogleRow,
                            null,
                            oidcUser.getName(),
                            Roles.ROLE_USER,
                            oidcUser.getEmail(),
                            oidcUser.getLocale(),
                            null,
                            DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                            oidcUser.getSubject()));
        } else {
            userRow = userRowOptional.get();
        }
        final OAuth2UserAuthority authority = new OAuth2UserAuthority(userRow.getRole(), oidcUser.getAttributes());
        return new DefaultOidcUser(
                Set.of(authority),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }
}
