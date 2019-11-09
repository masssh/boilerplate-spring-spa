package masssh.boilerplate.spring.spa.web.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class ApplicationUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
        final Optional<UserRow> userOptional = userDao.single(userId);
        if (userOptional.isPresent()) {
            return new ApplicationUserDetails(userOptional.get());
        }
        throw new UsernameNotFoundException("user not found.");
    }
}
