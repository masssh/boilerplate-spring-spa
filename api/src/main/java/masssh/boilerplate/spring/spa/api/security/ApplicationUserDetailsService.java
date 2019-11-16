package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
        final Optional<UserRow> userOptional = userDao.single(userId);
        if (userOptional.isPresent()) {
            return new ApplicationUserDetails(userOptional.get());
        }
        throw new UsernameNotFoundException("user not found.");
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        final String userId = (String) token.getPrincipal();
        final String accessToken = (String) token.getCredentials();
        final Optional<UserRow> userOptional = userDao.single(userId);
        if (userOptional.isPresent() && Objects.equals(userOptional.get().getAccessToken(), accessToken)) {
            return new ApplicationUserDetails(userOptional.get());
        }
        throw new UsernameNotFoundException("user not found.");
    }
}
