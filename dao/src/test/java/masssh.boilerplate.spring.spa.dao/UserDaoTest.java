package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.testutil.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OAuth2GoogleDao oAuth2GoogleDao;
    @Autowired
    private UserFactory userFactory;

    @Test
    void crud() {
        assertThat(userDao.single("userId")).isEmpty();
        final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        final UserRow before = new UserRow(null, "userId", "userName", "role", "email", "locale", "passwordHash", "accessToken", null, now, now);
        userDao.create(before);
        UserRow inserted = userDao.single("userId").orElseThrow(AssertionError::new);
        assertThat(inserted.getUserId()).isEqualTo("userId");
        assertThat(inserted.getUserName()).isEqualTo("userName");
        assertThat(inserted.getRole()).isEqualTo("role");
        assertThat(inserted.getEmail()).isEqualTo("email");
        assertThat(inserted.getLocale()).isEqualTo("locale");
        assertThat(inserted.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(inserted.getAccessToken()).isEqualTo("accessToken");
        assertThat(inserted.getGoogleSubject()).isEqualTo(null);
        assertThat(inserted.getCreatedAt()).isAfterOrEqualTo(before.getCreatedAt());
        assertThat(inserted.getUpdatedAt()).isAfterOrEqualTo(before.getUpdatedAt());

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
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(inserted.getUpdatedAt());

        userDao.delete(updated.getUserId());
        assertThat(userDao.single("userId")).isEmpty();
    }

    @Test
    void join() {
        final UserRow userRow = userFactory.builder().createWithOAuth2Google();
        final OAuth2GoogleRow oAuth2GoogleRow = userRow.getOAuth2GoogleRow();
        final UserRow result = userDao.singleOAuth2Detail(userRow.getGoogleSubject()).orElseThrow(AssertionError::new);
        assertThat(result.getOAuth2GoogleRow()).isNotNull();
        assertThat(result.getOAuth2GoogleRow().getSubject()).isEqualTo(oAuth2GoogleRow.getSubject());
        assertThat(result.getOAuth2GoogleRow().getIdToken()).isEqualTo(oAuth2GoogleRow.getIdToken());
        assertThat(result.getOAuth2GoogleRow().getAccessToken()).isEqualTo(oAuth2GoogleRow.getAccessToken());
        assertThat(result.getOAuth2GoogleRow().getIssuedAt()).isEqualTo(oAuth2GoogleRow.getIssuedAt());
        assertThat(result.getOAuth2GoogleRow().getExpiresAt()).isEqualTo(oAuth2GoogleRow.getExpiresAt());
    }
}
