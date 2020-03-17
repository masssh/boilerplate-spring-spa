package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class OAuth2GoogleDaoTest {
    private static final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    @Autowired
    private OAuth2GoogleDao oAuth2GoogleDao;

    static OAuth2GoogleRow createRow() {
        return new OAuth2GoogleRow("subject", "idToken", "accessToken", now, now);
    }

    @Test
    void crud() {
        assertThat(oAuth2GoogleDao.single("subject")).isEmpty();
        oAuth2GoogleDao.create(createRow());
        OAuth2GoogleRow row = oAuth2GoogleDao.single("subject").orElseThrow(AssertionError::new);
        assertThat(row.getSubject()).isEqualTo("subject");
        assertThat(row.getIdToken()).isEqualTo("idToken");
        assertThat(row.getAccessToken()).isEqualTo("accessToken");
        assertThat(row.getIssuedAt()).isEqualTo(now);
        assertThat(row.getExpiresAt()).isEqualTo(now);

        row.setIdToken("updated");
        row.setAccessToken("updated");
        row.setIssuedAt(now.plusSeconds(1));
        row.setExpiresAt(now.plusSeconds(1));
        oAuth2GoogleDao.update(row);

        row = oAuth2GoogleDao.single("subject").orElseThrow(AssertionError::new);
        assertThat(row.getIdToken()).isEqualTo("updated");
        assertThat(row.getAccessToken()).isEqualTo("updated");
        assertThat(row.getIssuedAt()).isEqualTo(now.plusSeconds(1));
        assertThat(row.getExpiresAt()).isEqualTo(now.plusSeconds(1));

        oAuth2GoogleDao.delete(row.getSubject());
        assertThat(oAuth2GoogleDao.single("subject")).isEmpty();
    }
}
