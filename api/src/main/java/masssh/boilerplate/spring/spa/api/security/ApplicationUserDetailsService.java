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
import org.springframework.util.StringUtils;
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
        if (userOptional.isPresent()) {
            authenticateAccessToken(userOptional.get(), accessToken);
            return new ApplicationUserDetails(userOptional.get());
        }
        throw new UsernameNotFoundException("user not found.");
    }

    private void authenticateAccessToken(final UserRow user, final String accessToken) {
        if (StringUtils.isEmpty(user.getAccessToken())) {
            throw new UsernameNotFoundException("user not found.");
        }
        if (!user.getAccessToken().equals(accessToken)) {
            user.setAccessToken(null);
            userDao.update(user);
            throw new UsernameNotFoundException("user not found.");
        }
    }
}
