package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OAuth2GoogleDao oAuth2GoogleDao;

    static UserRow createRow() {
        return new UserRow(null, "userId", "userName", "roleUser", "email", Locale.JAPAN.toString(), "passwordHash", "accessToken", null, null, null);
    }

    @Test
    void crud() {
        assertThat(userDao.single("userId")).isEmpty();
        final UserRow before = createRow();
        userDao.create(before);
        UserRow inserted = userDao.single("userId").orElseThrow(AssertionError::new);
        assertThat(inserted.getUserId()).isEqualTo("userId");
        assertThat(inserted.getUserName()).isEqualTo("userName");
        assertThat(inserted.getRole()).isEqualTo("roleUser");
        assertThat(inserted.getEmail()).isEqualTo("email");
        assertThat(inserted.getLocale()).isEqualTo(Locale.JAPAN.toString());
        assertThat(inserted.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(inserted.getAccessToken()).isEqualTo("accessToken");
        assertThat(inserted.getGoogleSubject()).isEqualTo(null);
        assertThat(inserted.getCreatedAt()).isEqualTo(before.getCreatedAt());
        assertThat(inserted.getUpdatedAt()).isEqualTo(before.getUpdatedAt());

        inserted.setUserName("updated");
        inserted.setRole("updated");
        inserted.setEmail("updated");
        inserted.setLocale("updated");
        inserted.setPasswordHash("updated");
        inserted.setAccessToken("updated");
        userDao.update(inserted);

        final UserRow updated = userDao.single("userId").orElseThrow(AssertionError::new);
        assertThat(updated.getUserName()).isEqualTo("updated");
        assertThat(updated.getRole()).isEqualTo("updated");
        assertThat(updated.getEmail()).isEqualTo("updated");
        assertThat(updated.getLocale()).isEqualTo("updated");
        assertThat(updated.getPasswordHash()).isEqualTo("updated");
        assertThat(updated.getAccessToken()).isEqualTo("updated");
        assertThat(updated.getCreatedAt()).isEqualTo(inserted.getCreatedAt());
        assertThat(updated.getCreatedAt()).isAfterOrEqualTo(inserted.getCreatedAt());

        userDao.delete(updated.getUserId());
        assertThat(userDao.single("userId")).isEmpty();
    }

    @Test
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
