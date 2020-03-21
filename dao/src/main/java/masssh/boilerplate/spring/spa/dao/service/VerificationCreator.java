package masssh.boilerplate.spring.spa.dao.service;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.VerificationDao;
import masssh.boilerplate.spring.spa.enums.VerificationType;
import masssh.boilerplate.spring.spa.model.row.VerificationRow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationCreator {
    private final VerificationDao verificationDao;

    @Transactional
    public VerificationRow tryCreate(final long userId, final VerificationType verificationType, final Instant expiresAt)
            throws SQLIntegrityConstraintViolationException {
        for (int i = 0; i < 10; i++) {
            final String verificationHash = RandomService.randomAlphaNumeric(30);
            synchronized (this) {
                final Optional<VerificationRow> rowOptional = verificationDao.singleByVerificationHash(verificationHash);
                if (rowOptional.isEmpty()) {
                    final VerificationRow row = new VerificationRow(0L, verificationHash, userId, verificationType, expiresAt, null, null);
                    verificationDao.create(row);
                    return row;
                }
            }
        }
        throw new IllegalStateException("Failed to generate userHash.");
    }
}
