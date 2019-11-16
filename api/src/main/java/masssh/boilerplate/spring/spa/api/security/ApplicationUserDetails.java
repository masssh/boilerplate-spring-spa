package masssh.boilerplate.spring.spa.api.security;

import lombok.Data;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import java.util.Collection;

@Data
class ApplicationUserDetails implements UserDetails {
    private final UserRow userRow;

    ApplicationUserDetails(final UserRow userRow) {
        this.userRow = userRow;
    }

    public String getUsername() {
        return userRow.getUserId();
    }

    public String getPassword() {
        return userRow.getAccessToken();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userRow.getRole());
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return !StringUtils.isEmpty(userRow.getAccessToken());
    }
}
