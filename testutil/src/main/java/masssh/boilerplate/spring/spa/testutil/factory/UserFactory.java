package masssh.boilerplate.spring.spa.testutil.factory;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import masssh.boilerplate.spring.spa.dao.service.UserCreator;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final UserCreator userCreator;
    private final OAuth2GoogleFactory oAuth2GoogleFactory;

    public UserBuilder builder() {
        return new UserBuilder(userCreator, oAuth2GoogleFactory);
    }

    @Data
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    public static class UserBuilder {
        private final UserCreator userCreator;
        private final OAuth2GoogleFactory oAuth2GoogleFactory;
        private OAuth2GoogleRow oAuth2GoogleRow;
        private String userName = "userName";
        private String role = "USER";
        private String email = "email@example.com";
        private String locale = Locale.JAPAN.toString();
        private String passwordHash = "passwordHash";
        private String accessToken = "accessToken";
        private String googleSubject;

        public UserRow create() {
            final UserRow userRow = new UserRow();
            userRow.setOAuth2GoogleRow(oAuth2GoogleRow);
            userRow.setUserName(userName);
            userRow.setRole(role);
            userRow.setEmail(email);
            userRow.setLocale(locale);
            userRow.setPasswordHash(passwordHash);
            userRow.setAccessToken(accessToken);
            userRow.setGoogleSubject(googleSubject);
            return userCreator.tryCreate(userRow);
        }

        public UserRow createWithOAuth2Google() {
            final OAuth2GoogleRow row = oAuth2GoogleFactory.builder().create();
            return this.oAuth2GoogleRow(row).googleSubject(row.getSubject()).create();
        }
    }
}
