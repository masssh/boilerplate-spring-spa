package masssh.boilerplate.spring.spa.dao.service;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserDao userDao;

    @Transactional
    public UserRow tryCreate(final UserRow userRow) throws SQLIntegrityConstraintViolationException {
        if (userDao.singleByEmail(userRow.getEmail()).isPresent()) {
            throw new SQLIntegrityConstraintViolationException("Unique key violation: email]");
        }
        for (int i = 0; i < 10; i++) {
            final String userHash = RandomService.randomAlphaNumeric(10);
            synchronized (this) {
                final Optional<UserRow> maybeUser = userDao.singleByUserHash(userHash);
                if (maybeUser.isEmpty()) {
                    userRow.setUserHash(userHash);
                    userDao.create(userRow);
                    return userRow;
                }
            }
        }
        throw new IllegalStateException("Failed to generate userHash.");
    }
}
