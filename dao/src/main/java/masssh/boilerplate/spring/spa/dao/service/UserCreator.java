package masssh.boilerplate.spring.spa.dao.service;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserDao userDao;

    @Transactional
    public UserRow tryCreate(final UserRow userRow) {
        int count = 0;
        while (true) {
            final String userId = RandomService.randomAlphaNumeric(10);
            synchronized (this) {
                final Optional<UserRow> maybeUser = userDao.single(userId);
                if (maybeUser.isEmpty()) {
                    userRow.setUserId(userId);
                    userDao.create(userRow);
                    break;
                }
            }
            if (count >= 10) throw new IllegalStateException("Failed to generate userId hash.");
            count++;
        }
        return userRow;
    }
}
