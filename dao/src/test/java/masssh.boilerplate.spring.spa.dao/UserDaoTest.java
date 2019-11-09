package masssh.boilerplate.spring.spa.dao;

import static org.assertj.core.api.Assertions.*;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@DaoTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OAuth2GoogleDao oAuth2GoogleDao;

    static UserRow createRow() {
        return new UserRow(null, "userId", "userName", "roleUser", "email", Locale.JAPAN.toString(), "passwordHash", "accessToken", null);
    }

    @Test
    @Transactional
    void crud() {
        assertThat(userDao.single("userId")).isEmpty();
        userDao.create(createRow());
        UserRow userRow = userDao.single("userId").orElseThrow(AssertionError::new);
        assertThat(userRow.getUserId()).isEqualTo("userId");
        assertThat(userRow.getUserName()).isEqualTo("userName");
        assertThat(userRow.getRole()).isEqualTo("roleUser");
        assertThat(userRow.getEmail()).isEqualTo("email");
        assertThat(userRow.getLocale()).isEqualTo(Locale.JAPAN.toString());
        assertThat(userRow.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(userRow.getAccessToken()).isEqualTo("accessToken");
        assertThat(userRow.getGoogleSubject()).isEqualTo(null);

        userRow.setUserName("updated");
        userRow.setRole("updated");
        userRow.setEmail("updated");
        userRow.setLocale("updated");
        userRow.setPasswordHash("updated");
        userRow.setAccessToken("updated");
        userDao.update(userRow);

        userRow = userDao.single("userId").orElseThrow(AssertionError::new);
        assertThat(userRow.getUserName()).isEqualTo("updated");
        assertThat(userRow.getRole()).isEqualTo("updated");
        assertThat(userRow.getEmail()).isEqualTo("updated");
        assertThat(userRow.getLocale()).isEqualTo("updated");
        assertThat(userRow.getPasswordHash()).isEqualTo("updated");
        assertThat(userRow.getAccessToken()).isEqualTo("updated");

        userDao.delete(userRow.getUserId());
        assertThat(userDao.single("userId")).isEmpty();
    }

    @Test
    @Transactional
    void join() {
        final UserRow userRow = createRow();
        final OAuth2GoogleRow oAuth2GoogleRow = OAuth2GoogleDaoTest.createRow();
        userRow.setGoogleSubject(oAuth2GoogleRow.getSubject());
        oAuth2GoogleDao.create(oAuth2GoogleRow);
        userDao.create(userRow);
        final UserRow result = userDao.singleOAuth2Detail(userRow.getGoogleSubject()).orElseThrow(AssertionError::new);
        assertThat(result.getOAuth2GoogleRow()).isNotNull();
        assertThat(result.getOAuth2GoogleRow().getSubject()).isEqualTo("subject");
        assertThat(result.getOAuth2GoogleRow().getIdToken()).isEqualTo("idToken");
        assertThat(result.getOAuth2GoogleRow().getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getOAuth2GoogleRow().getIssuedAt()).isEqualTo(100);
        assertThat(result.getOAuth2GoogleRow().getExpiresAt()).isEqualTo(200);
    }
}
