package masssh.boilerplate.spring.web.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.web.dao.UserDao;
import masssh.boilerplate.spring.web.model.row.UserRow;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class ApplicationUserService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserRow> userOptional = userDao.single(username);
        if (userOptional.isPresent()) {
            return new ApplicationUser(userOptional.get());
        }
        throw new UsernameNotFoundException("user not found.");
    }
}
