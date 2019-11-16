package masssh.boilerplate.spring.spa.api.security;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.Set;

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
            userRowOptional = userDao.singleByEmail(oidcUser.getEmail());
            if (userRowOptional.isPresent()) {
                userRow = userRowOptional.get();
                updateUserAttributes(userRow, oidcUser);
            } else {
                final OAuth2GoogleRow newOAuth2GoogleRow = new OAuth2GoogleRow(
                        oidcUser.getSubject(),
                        oidcUser.getIdToken().getTokenValue(),
                        oidcUser.getAccessTokenHash(),
                        oidcUser.getIssuedAt().getEpochSecond(),
                        oidcUser.getExpiresAt().getEpochSecond());
                oAuth2GoogleDao.create(newOAuth2GoogleRow);

                userRow = userCreator.tryCreate(
                        new UserRow(newOAuth2GoogleRow,
                                null,
                                oidcUser.getName(),
                                Roles.ROLE_USER,
                                oidcUser.getEmail(),
                                oidcUser.getLocale(),
                                null,
                                null,
                                oidcUser.getSubject()));
            }
        } else {
            userRow = userRowOptional.get();
            updateUserAttributes(userRow, oidcUser);
        }
        final OAuth2UserAuthority authority = new OAuth2UserAuthority(userRow.getRole(), oidcUser.getAttributes());
        return new DefaultOidcUser(
                Set.of(authority),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }

    private void updateUserAttributes(final UserRow userRow, final OidcUser oidcUser) {
        final OAuth2GoogleRow oAuth2GoogleRow = oAuth2GoogleDao.single(oidcUser.getSubject())
                                                        .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR));
        oAuth2GoogleRow.setIdToken(oidcUser.getIdToken().getTokenValue());
        oAuth2GoogleRow.setAccessToken(oidcUser.getAccessTokenHash());
        oAuth2GoogleRow.setIssuedAt(oidcUser.getIssuedAt().getEpochSecond());
        oAuth2GoogleRow.setExpiresAt(oidcUser.getExpiresAt().getEpochSecond());
        oAuth2GoogleDao.update(oAuth2GoogleRow);

        userRow.setUserName(oidcUser.getName());
        userRow.setLocale(oidcUser.getLocale());
        userRow.setGoogleSubject(oidcUser.getSubject());
    }
}
