package masssh.boilerplate.spring.spa.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
}
