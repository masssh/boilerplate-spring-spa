package masssh.boilerplate.spring.spa.testutil.factory;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import masssh.boilerplate.spring.spa.dao.OAuth2GoogleDao;
import masssh.boilerplate.spring.spa.dao.service.RandomService;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class OAuth2GoogleFactory {
    private final OAuth2GoogleDao oAuth2GoogleDao;

    public OAuth2GoogleBuilder builder() {
        return new OAuth2GoogleBuilder(oAuth2GoogleDao);
    }

    @Accessors(fluent = true)
    @RequiredArgsConstructor
    public static class OAuth2GoogleBuilder {
        private final OAuth2GoogleDao oAuth2GoogleDao;
        private static Instant defaultIssuedAt;
        private static Instant defaultExpiresAt;

        static {
            final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            defaultIssuedAt = now;
            defaultExpiresAt = now.plusSeconds(60 * 60 * 24);
        }

        private String subject = "subject";
        private String idToken = "idToken";
        private String accessToken = "accessToken";
        private Instant issuedAt = defaultIssuedAt;
        private Instant expiresAt = defaultExpiresAt;

        public OAuth2GoogleRow create() {
            final OAuth2GoogleRow row = new OAuth2GoogleRow();
            row.setSubject(subject);
            row.setIdToken(idToken);
            row.setAccessToken(accessToken);
            row.setIssuedAt(issuedAt);
            row.setExpiresAt(expiresAt);

            int count = 0;
            while (true) {
                final String subject = RandomService.randomAlphaNumeric(10);
                synchronized (this) {
                    if (oAuth2GoogleDao.single(subject).isEmpty()) {
                        row.setSubject(subject);
                        oAuth2GoogleDao.create(row);
                        break;
                    }
                }
                if (count >= 10) throw new IllegalStateException("Failed to generate subject.");
                count++;
            }
            return row;

        }
    }
}
