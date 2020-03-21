package masssh.boilerplate.spring.spa.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masssh.boilerplate.spring.spa.api.security.ApplicationUserDetails;
import masssh.boilerplate.spring.spa.api.security.Roles;
import masssh.boilerplate.spring.spa.dao.OAuth2GoogleDao;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.dao.VerificationDao;
import masssh.boilerplate.spring.spa.dao.service.RandomService;
import masssh.boilerplate.spring.spa.dao.service.UserCreator;
import masssh.boilerplate.spring.spa.dao.service.VerificationCreator;
import masssh.boilerplate.spring.spa.enums.VerificationType;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.model.row.VerificationRow;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final VerificationDao verificationDao;
    private final UserCreator userCreator;
    private final VerificationCreator verificationCreator;
    private final OAuth2GoogleDao oAuth2GoogleDao;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<UserRow> userOptional = userDao.singleByEmail(email);
        if (userOptional.isPresent()) {
            final UserRow user = userOptional.get();
            return new ApplicationUserDetails(user);
        }
        throw new UsernameNotFoundException("user not found.");
    }

    public Optional<UserRow> loadUserByPrincipal(final Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        final Authentication authentication = (Authentication) principal;
        if (authentication.getPrincipal() instanceof ApplicationUserDetails) {
            return Optional.of(((ApplicationUserDetails) authentication.getPrincipal()).getUserRow());
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            final String subject = ((DefaultOidcUser) authentication.getPrincipal()).getSubject();
            return userDao.singleBySubject(subject);
        }
        return Optional.empty();
    }

    @Transactional
    public UserRow registerUserByEmail(final String userName, final String password, final String email, final Locale locale) throws SQLIntegrityConstraintViolationException {
        if (userDao.singleByEmail(email).isPresent()) {
            throw new SQLIntegrityConstraintViolationException();
        }
        final UserRow userRow = createUser(userName, Roles.ROLE_NOT_VERIFIED, password, email, locale.toLanguageTag(), null);
        final VerificationRow verificationRow = verificationCreator.tryCreate(userRow.getUserId(),
                VerificationType.EMAIL,
                Instant.now().plusSeconds(60 * 60 * 24));
        mailService.sendEmailVerification(email, verificationRow.getVerificationHash());
        return userRow;
    }

    @Transactional
    public UserRow registerOidcUser(final OidcUser oidcUser) throws SQLIntegrityConstraintViolationException {
        Optional<UserRow> userRowOptional = userDao.singleBySubject(oidcUser.getSubject());
        UserRow userRow;
        if (userRowOptional.isEmpty()) {
            userRowOptional = userDao.singleByEmail(oidcUser.getEmail());
            if (userRowOptional.isPresent()) {
                userRow = userRowOptional.get();
                createOAuth2Google(oidcUser);
                updateSubjectOnUser(userRow, oidcUser.getSubject());
            } else {
                createOAuth2Google(oidcUser);
                userRow = createUser(oidcUser.getClaim("name"),
                        Roles.ROLE_USER,
                        null,
                        oidcUser.getEmail(),
                        oidcUser.getLocale(),
                        oidcUser.getSubject());
            }
        } else {
            userRow = userRowOptional.get();
            updateOAuth2Google(oidcUser);
        }
        return userRow;
    }

    @Transactional
    public void verifyUserByEmail(final String verificationHash) {
        verificationDao.singleByVerificationHash(verificationHash)
                .ifPresent(verification -> {
                    if (VerificationType.EMAIL == verification.getVerificationType()) {
                        userDao.single(verification.getUserId()).ifPresent(user -> {
                            if (Roles.ROLE_NOT_VERIFIED.equals(user.getRole())) {
                                user.setRole(Roles.ROLE_USER);
                                userDao.update(user);
                            }
                        });
                    }
                });
    }

    @Transactional
    public void deleteUser(final String userHash) {
        userDao.singleByUserHash(userHash).ifPresent(userRow -> {
            userDao.delete(userRow.getUserId());
            Optional.ofNullable(userRow.getGoogleSubject()).ifPresent(oAuth2GoogleDao::deleteBySubject);
        });
    }

    public String generatePasswordHash(final String password) {
        return passwordEncoder.encode(password);
    }

    private void createOAuth2Google(final OidcUser oidcUser) {
        final OAuth2GoogleRow oAuth2GoogleRow = new OAuth2GoogleRow(
                0,
                oidcUser.getSubject(),
                oidcUser.getIdToken().getTokenValue(),
                oidcUser.getAccessTokenHash(),
                oidcUser.getIssuedAt(),
                oidcUser.getExpiresAt(),
                null,
                null);
        oAuth2GoogleDao.create(oAuth2GoogleRow);
    }

    private UserRow createUser(final String userName, final String role, final String password,
                               final String email, final String locale, final String subject) throws SQLIntegrityConstraintViolationException {
        return userCreator.tryCreate(
                new UserRow(null,
                        0,
                        null,
                        userName,
                        role,
                        email,
                        locale,
                        StringUtils.isEmpty(password) ? null : passwordEncoder.encode(password),
                        null,
                        subject,
                        null,
                        null));
    }

    private void updateOAuth2Google(final OidcUser oidcUser) {
        final OAuth2GoogleRow oAuth2GoogleRow = oAuth2GoogleDao.singleBySubject(oidcUser.getSubject())
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR));
        oAuth2GoogleRow.setIdToken(oidcUser.getIdToken().getTokenValue());
        oAuth2GoogleRow.setAccessToken(oidcUser.getAccessTokenHash());
        oAuth2GoogleRow.setIssuedAt(oidcUser.getIssuedAt());
        oAuth2GoogleRow.setExpiresAt(oidcUser.getExpiresAt());
        oAuth2GoogleDao.update(oAuth2GoogleRow);
    }

    public void updateUser(final UserRow userRow) {
        userDao.update(userRow);
    }

    private void updateSubjectOnUser(final UserRow userRow, final String subject) throws
            SQLIntegrityConstraintViolationException {
        userRow.setGoogleSubject(subject);
        userDao.updateGoogleSubject(userRow);
    }

    public void refreshAccessToken(final UserRow userRow) {
        userRow.setAccessToken(generateAccessToken());
        userDao.update(userRow);
    }

    private String generateAccessToken() {
        return RandomService.randomAlphaNumeric(30);
    }
}
