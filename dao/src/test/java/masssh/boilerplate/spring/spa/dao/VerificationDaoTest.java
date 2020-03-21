package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.dao.annotation.DaoTest;
import masssh.boilerplate.spring.spa.enums.VerificationType;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import masssh.boilerplate.spring.spa.model.row.VerificationRow;
import masssh.boilerplate.spring.spa.testutil.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class VerificationDaoTest {
    @Autowired
    private VerificationDao verificationDao;
    @Autowired
    private UserFactory userFactory;

    @Test
    void crud() throws SQLIntegrityConstraintViolationException {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        final UserRow userRow1 = userFactory.builder().create();
        final VerificationRow before = new VerificationRow(0, "verificationHash", userRow1.getUserId(), VerificationType.EMAIL, now, now, now);
        verificationDao.create(before);
        final long verificationId = before.getVerificationId();
        VerificationRow inserted = verificationDao.single(verificationId).orElseThrow(AssertionError::new);
        assertThat(inserted.getVerificationId()).isEqualTo(verificationId);
        assertThat(inserted.getVerificationHash()).isEqualTo("verificationHash");
        assertThat(inserted.getUserId()).isEqualTo(userRow1.getUserId());
        assertThat(inserted.getVerificationType()).isEqualTo(VerificationType.EMAIL);
        assertThat(inserted.getExpiresAt()).isEqualTo(now);
        assertThat(inserted.getCreatedAt()).isAfterOrEqualTo(before.getCreatedAt());
        assertThat(inserted.getUpdatedAt()).isAfterOrEqualTo(before.getUpdatedAt());

        final UserRow userRow2 = userFactory.builder().create();
        inserted.setVerificationType(VerificationType.PASSWORD);
        inserted.setExpiresAt(now.plusSeconds(1));
        verificationDao.update(inserted);

        final VerificationRow updated = verificationDao.single(verificationId).orElseThrow(AssertionError::new);
        assertThat(updated.getVerificationType()).isEqualTo(VerificationType.PASSWORD);
        assertThat(updated.getExpiresAt()).isEqualTo(now.plusSeconds(1));
        assertThat(updated.getCreatedAt()).isEqualTo(inserted.getCreatedAt());
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(inserted.getUpdatedAt());

        verificationDao.delete(updated.getVerificationId());
        assertThat(verificationDao.single(verificationId)).isEmpty();
    }
}
