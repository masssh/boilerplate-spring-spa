package masssh.boilerplate.spring.web.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.web.dao.OAuth2GoogleDao;
import masssh.boilerplate.spring.web.dao.UserDao;
import masssh.boilerplate.spring.web.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.web.model.row.UserRow;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
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
    private final OAuth2GoogleDao oAuth2GoogleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        final OidcUser oidcUser = super.loadUser(userRequest);
        Optional<UserRow> userRowOptional = userDao.singleBySubject(oidcUser.getSubject());
        UserRow userRow;
        if (userRowOptional.isEmpty()) {
            final OAuth2GoogleRow newOauth2GoogleRow = new OAuth2GoogleRow(oidcUser);
            oAuth2GoogleDao.create(newOauth2GoogleRow);

            int count = 0;
            while (true) {
                final String userId = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
                final Optional<UserRow> maybeUser = userDao.single(userId);
                if (maybeUser.isEmpty()) {
                    userRow = new UserRow(
                            newOauth2GoogleRow,
                            userId,
                            oidcUser.getName(),
                            Roles.ROLE_USER,
                            oidcUser.getEmail(),
                            oidcUser.getLocale(),
                            null,
                            DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()),
                            oidcUser.getSubject());
                    userDao.create(userRow);
                    break;
                }
                if (count >= 10) throw new IllegalStateException("Failed to generate userId hash.");
                count++;
            }
        } else {
            userRow = userRowOptional.get();
        }
        final OAuth2UserAuthority authority = new OAuth2UserAuthority(userRow.getRole(), oidcUser.getAttributes());
        return new ApplicationUserDetails(
                userRow,
                Set.of(authority),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }
}
