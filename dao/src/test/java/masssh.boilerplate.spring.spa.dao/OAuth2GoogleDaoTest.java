package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.testutil.factory.OAuth2GoogleFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class OAuth2GoogleDaoTest {
    @Autowired
    private OAuth2GoogleDao oAuth2GoogleDao;
    @Autowired
    private OAuth2GoogleFactory oAuth2GoogleFactory;

    @Test
    void crud() {
        assertThat(oAuth2GoogleDao.single("subject")).isEmpty();
        final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        final OAuth2GoogleRow before = new OAuth2GoogleRow("subject", "idToken", "accessToken", now, now, null, null);
        oAuth2GoogleDao.create(before);
        OAuth2GoogleRow inserted = oAuth2GoogleDao.single("subject").orElseThrow(AssertionError::new);
        assertThat(inserted.getSubject()).isEqualTo("subject");
        assertThat(inserted.getIdToken()).isEqualTo("idToken");
        assertThat(inserted.getAccessToken()).isEqualTo("accessToken");
        assertThat(inserted.getIssuedAt()).isEqualTo(now);
        assertThat(inserted.getExpiresAt()).isEqualTo(now);
        assertThat(inserted.getCreatedAt()).isEqualTo(before.getCreatedAt());
        assertThat(inserted.getUpdatedAt()).isEqualTo(before.getUpdatedAt());

        inserted.setIdToken("updated");
        inserted.setAccessToken("updated");
        inserted.setIssuedAt(now.plusSeconds(1));
        inserted.setExpiresAt(now.plusSeconds(1));
        oAuth2GoogleDao.update(inserted);

        final OAuth2GoogleRow updated = oAuth2GoogleDao.single("subject").orElseThrow(AssertionError::new);
        assertThat(updated.getIdToken()).isEqualTo("updated");
        assertThat(updated.getAccessToken()).isEqualTo("updated");
        assertThat(updated.getIssuedAt()).isEqualTo(now.plusSeconds(1));
        assertThat(updated.getExpiresAt()).isEqualTo(now.plusSeconds(1));
        assertThat(updated.getCreatedAt()).isEqualTo(inserted.getCreatedAt());
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(inserted.getUpdatedAt());

        oAuth2GoogleDao.delete(updated.getSubject());
        assertThat(oAuth2GoogleDao.single("subject")).isEmpty();
    }
}
