package masssh.boilerplate.spring.spa.web.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import java.util.Collection;
import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = true)
class ApplicationUserDetails extends DefaultOidcUser implements UserDetails {
    private final UserRow userRow;

    ApplicationUserDetails(final UserRow userRow) {
        super(Collections.emptyList(), new OidcIdToken(null, null, null, null));
        this.userRow = userRow;
    }

    ApplicationUserDetails(final UserRow userRow, Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo, String nameAttributeKey) {
        super(authorities, idToken, userInfo, nameAttributeKey);
        this.userRow = userRow;
    }

    public String getUsername() {
        return userRow.getUserId();
    }

    public String getPassword() {
        return userRow.getPasswordHash();
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
        return true;
    }
}
