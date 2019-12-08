package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.dao.UserDao;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UserDao userDao;
    private final UserService userService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        final String userId = (String) token.getPrincipal();
        final String accessToken = (String) token.getCredentials();
        final Optional<UserRow> userOptional = userDao.single(userId);
        if (userOptional.isPresent()) {
            validateAccessToken(userOptional.get(), accessToken);
            return new ApplicationUserDetails(userOptional.get());
        }
        throw new UsernameNotFoundException("user not found.");
    }

    private void validateAccessToken(final UserRow user, final String accessToken) {
        if (!userService.validateAccessToken(user, accessToken)) {
            throw new UsernameNotFoundException("user not found.");
        }
    }
}
