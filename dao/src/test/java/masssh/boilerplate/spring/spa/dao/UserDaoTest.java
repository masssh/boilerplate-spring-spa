package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.testutil.factory.OAuth2GoogleFactory;
import masssh.boilerplate.spring.spa.testutil.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private OAuth2GoogleFactory oAuth2GoogleFactory;

    @Test
    void crud() throws SQLIntegrityConstraintViolationException {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        final UserRow before = new UserRow(null, 0, "userHash", "userName", "role", "email", "locale", "passwordHash", "accessToken", null, now, now);
        userDao.create(before);
        final long userId = before.getUserId();
        UserRow inserted = userDao.single(userId).orElseThrow(AssertionError::new);
        assertThat(inserted.getUserId()).isEqualTo(userId);
        assertThat(inserted.getUserHash()).isEqualTo("userHash");
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
        inserted.setLocale("updated");
        inserted.setPasswordHash("updated");
        inserted.setAccessToken("updated");
        userDao.update(inserted);

        final UserRow updated1 = userDao.single(userId).orElseThrow(AssertionError::new);
        assertThat(updated1.getUserName()).isEqualTo("updated");
        assertThat(updated1.getRole()).isEqualTo("updated");
        assertThat(updated1.getLocale()).isEqualTo("updated");
        assertThat(updated1.getPasswordHash()).isEqualTo("updated");
        assertThat(updated1.getAccessToken()).isEqualTo("updated");
        assertThat(updated1.getCreatedAt()).isEqualTo(inserted.getCreatedAt());
        assertThat(updated1.getUpdatedAt()).isAfterOrEqualTo(inserted.getUpdatedAt());

        final OAuth2GoogleRow oAuth2GoogleRow = oAuth2GoogleFactory.builder().create();
        updated1.setGoogleSubject(oAuth2GoogleRow.getSubject());
        userDao.updateGoogleSubject(updated1);

        final UserRow updated2 = userDao.single(userId).orElseThrow(AssertionError::new);
        assertThat(updated2.getGoogleSubject()).isEqualTo(oAuth2GoogleRow.getSubject());

        userDao.delete(userId);
        assertThat(userDao.single(userId)).isEmpty();
    }

    @Test
    void join() throws SQLIntegrityConstraintViolationException {
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
