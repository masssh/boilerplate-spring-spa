package masssh.boilerplate.spring.web.security;

import lombok.Getter;
import masssh.boilerplate.spring.web.model.row.UserRow;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

class ApplicationUser implements UserDetails {
    @Getter
    private final String username;
    @Getter
    private final String password;
    @Getter
    private final Collection<? extends GrantedAuthority> authorities;
    @Getter
    private final boolean enabled;
    @Getter
    private final boolean accountNonLocked;
    @Getter
    private final boolean accountNonExpired;
    @Getter
    private final boolean credentialsNonExpired;

    ApplicationUser(final UserRow user) {
        username = user.getUserName();
        password = user.getUserToken();
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
        enabled = true;
        accountNonLocked = true;
        accountNonExpired = true;
        credentialsNonExpired = true;
    }
}
