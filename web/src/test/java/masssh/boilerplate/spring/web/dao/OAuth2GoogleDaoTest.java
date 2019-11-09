package masssh.boilerplate.spring.web.dao;

import static org.assertj.core.api.Assertions.*;

import masssh.boilerplate.spring.web.annotation.TestSetUp;
import masssh.boilerplate.spring.web.model.row.OAuth2GoogleRow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@TestSetUp
class OAuth2GoogleDaoTest {
    @Autowired
    private OAuth2GoogleDao oAuth2GoogleDao;

    public static OAuth2GoogleRow createRow() {
        return new OAuth2GoogleRow("subject", "idToken", "accessToken", 100, 200);
    }

    @Test
    @Transactional
    void crud() {
        assertThat(oAuth2GoogleDao.single("subject")).isEmpty();
        oAuth2GoogleDao.create(createRow());
        OAuth2GoogleRow row = oAuth2GoogleDao.single("subject").orElseThrow(AssertionError::new);
        assertThat(row.getSubject()).isEqualTo("subject");
        assertThat(row.getIdToken()).isEqualTo("idToken");
        assertThat(row.getAccessToken()).isEqualTo("accessToken");
        assertThat(row.getIssuedAt()).isEqualTo(100);
        assertThat(row.getExpiresAt()).isEqualTo(200);

        row.setIdToken("updated");
        row.setAccessToken("updated");
        row.setIssuedAt(999);
        row.setExpiresAt(999);
        oAuth2GoogleDao.update(row);

        row = oAuth2GoogleDao.single("userId").orElseThrow(AssertionError::new);
        assertThat(row.getIdToken()).isEqualTo("updated");
        assertThat(row.getAccessToken()).isEqualTo("updated");
        assertThat(row.getIssuedAt()).isEqualTo(999);
        assertThat(row.getExpiresAt()).isEqualTo(999);

        oAuth2GoogleDao.delete(row.getSubject());
        assertThat(oAuth2GoogleDao.single("subject")).isEmpty();
    }
}
