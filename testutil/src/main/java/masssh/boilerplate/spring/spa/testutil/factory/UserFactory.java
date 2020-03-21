package masssh.boilerplate.spring.spa.testutil.factory;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.dao.service.RandomService;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final UserDao userDao;
    private final OAuth2GoogleFactory oAuth2GoogleFactory;

    public UserBuilder builder() {
        return new UserBuilder(userDao, oAuth2GoogleFactory);
    }

    @Data
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    public static class UserBuilder {
        private final UserDao userDao;
        private final OAuth2GoogleFactory oAuth2GoogleFactory;
        private OAuth2GoogleRow oAuth2GoogleRow;
        private String userName = "userName";
        private String role = "USER";
        private String locale = Locale.JAPAN.toString();
        private String passwordHash = "passwordHash";
        private String accessToken = "accessToken";
        private String googleSubject;

        public UserRow create() throws SQLIntegrityConstraintViolationException {
            final UserRow userRow = new UserRow();
            userRow.setOAuth2GoogleRow(oAuth2GoogleRow);
            userRow.setUserName(userName);
            userRow.setRole(role);
            userRow.setLocale(locale);
            userRow.setPasswordHash(passwordHash);
            userRow.setAccessToken(accessToken);
            userRow.setGoogleSubject(googleSubject);
            for (int i = 0; i < 10; i++) {
                final String userHash = RandomService.randomAlphaNumeric(10);
                synchronized (this) {
                    if (userDao.singleByUserHash(userHash).isEmpty()) {
                        userRow.setUserHash(userHash);
                        userRow.setEmail(userHash + "@example.com");
                        userDao.create(userRow);
                        return userRow;
                    }
                }
            }
            throw new SQLIntegrityConstraintViolationException();
        }

        public UserRow createWithOAuth2Google() throws SQLIntegrityConstraintViolationException {
            final OAuth2GoogleRow row = oAuth2GoogleFactory.builder().create();
            return this.oAuth2GoogleRow(row).googleSubject(row.getSubject()).create();
        }
    }
}
