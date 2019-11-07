package masssh.boilerplate.spring.web.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import java.io.Serializable;

@Data
@ToString(exclude = {"idToken", "accessToken"})
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2GoogleRow implements Serializable {
    private String subject;
    private String idToken;
    private String accessToken;
    private long issuedAt;
    private long expiresAt;

    public OAuth2GoogleRow(final OidcUser oidcUser) {
        subject = oidcUser.getSubject();
        idToken = oidcUser.getIdToken().getTokenValue();
        accessToken = oidcUser.getAccessTokenHash();
        issuedAt = oidcUser.getIssuedAt().getEpochSecond();
        expiresAt = oidcUser.getExpiresAt().getEpochSecond();
    }
}
