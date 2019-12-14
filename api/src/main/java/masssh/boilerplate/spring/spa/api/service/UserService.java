package masssh.boilerplate.spring.spa.api.service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.security.ApplicationUserDetails;
import masssh.boilerplate.spring.spa.api.security.Roles;
import masssh.boilerplate.spring.spa.dao.OAuth2GoogleDao;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.dao.service.UserCreator;
import masssh.boilerplate.spring.spa.model.cookie.UserToken;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UserDao userDao;
    private final UserCreator userCreator;
    private final OAuth2GoogleDao oAuth2GoogleDao;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        final String userId = (String) token.getPrincipal();
        final String accessToken = (String) token.getCredentials();
        final Optional<UserRow> userOptional = userDao.single(userId);
        if (userOptional.isPresent()) {
            final UserRow user = userOptional.get();
            if (validateAccessToken(user, accessToken)) {
                return new ApplicationUserDetails(user);
            }
        }
        throw new UsernameNotFoundException("user not found.");
    }

    public Optional<UserRow> loadUserByEmail(final String email) {
        return userDao.singleByEmail(email);
    }

    public Optional<UserRow> loadUserByUserId(final String userId) {
        return userDao.single(userId);
    }

    public void registerUser(final String userName, final String password, final String email, final Locale locale) {
        createUser(userName, password, email, locale.toLanguageTag(), null);
    }

    public UserRow registerOidcUser(final OidcUser oidcUser) {
        Optional<UserRow> userRowOptional = userDao.singleBySubject(oidcUser.getSubject());
        UserRow userRow;
        if (userRowOptional.isEmpty()) {
            userRowOptional = userDao.singleByEmail(oidcUser.getEmail());
            if (userRowOptional.isPresent()) {
                userRow = userRowOptional.get();
                updateOAuth2Google(oidcUser);
                updateUser(userRow, oidcUser);
            } else {
                createOAuth2Google(oidcUser);
                userRow = createUser(oidcUser.getName(),
                        null,
                        oidcUser.getEmail(),
                        oidcUser.getLocale(),
                        oidcUser.getSubject());
            }
        } else {
            userRow = userRowOptional.get();
            updateOAuth2Google(oidcUser);
            updateUser(userRow, oidcUser);
        }
        return userRow;
    }

    private void createOAuth2Google(final OidcUser oidcUser) {
        final OAuth2GoogleRow oAuth2GoogleRow = new OAuth2GoogleRow(
                oidcUser.getSubject(),
                oidcUser.getIdToken().getTokenValue(),
                oidcUser.getAccessTokenHash(),
                oidcUser.getIssuedAt().getEpochSecond(),
                oidcUser.getExpiresAt().getEpochSecond());
        oAuth2GoogleDao.create(oAuth2GoogleRow);
    }

    private UserRow createUser(final String userName, final String password, final String email, final String locale, final String subject) {
        return userCreator.tryCreate(
                new UserRow(null,
                        null,
                        userName,
                        Roles.ROLE_USER,
                        email,
                        locale,
                        StringUtils.isEmpty(password) ? null : passwordEncoder.encode(password),
                        null,
                        subject));
    }

    private void updateOAuth2Google(final OidcUser oidcUser) {
        final OAuth2GoogleRow oAuth2GoogleRow = oAuth2GoogleDao.single(oidcUser.getSubject())
                                                        .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR));
        oAuth2GoogleRow.setIdToken(oidcUser.getIdToken().getTokenValue());
        oAuth2GoogleRow.setAccessToken(oidcUser.getAccessTokenHash());
        oAuth2GoogleRow.setIssuedAt(oidcUser.getIssuedAt().getEpochSecond());
        oAuth2GoogleRow.setExpiresAt(oidcUser.getExpiresAt().getEpochSecond());
        oAuth2GoogleDao.update(oAuth2GoogleRow);
    }

    private void updateUser(final UserRow userRow, final OidcUser oidcUser) {
        userRow.setUserName(oidcUser.getName());
        userRow.setEmail(oidcUser.getEmail());
        userRow.setLocale(oidcUser.getLocale());
        userRow.setAccessToken(generateAccessToken());
        userRow.setGoogleSubject(oidcUser.getSubject());
        userDao.update(userRow);
    }

    public boolean validateAccessToken(final UserRow userRow, final String accessToken) {
        if (StringUtils.isEmpty(userRow.getAccessToken())) {
            return false;
        }
        if (!userRow.getAccessToken().equals(accessToken)) {
            userRow.setAccessToken(null);
            userDao.update(userRow);
            return false;
        }
        return true;
    }

    public void refreshAccessToken(final UserRow userRow) {
        userRow.setAccessToken(generateAccessToken());
        userDao.update(userRow);
    }

    private String generateAccessToken() {
        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
    }

    public String encodeUserToken(final UserToken userToken) {
        if (userToken == null) {
            return null;
        }
        try {
            return Base64Utils.encodeToString(objectMapper.writeValueAsBytes(userToken));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize user token userId: {}", userToken.getUserId());
        }
        return null;
    }

    public UserToken decodeUserToken(final String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            return objectMapper.readValue(Base64Utils.decodeFromString(token), UserToken.class);
        } catch (IOException e) {
            log.error("Failed to deserialize user token token: {}", token);
        }
        return null;
    }
}
