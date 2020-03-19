package masssh.boilerplate.spring.spa.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Data
@ToString(exclude = {"idToken", "accessToken"})
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2GoogleRow implements Serializable {
    private String subject;
    private String idToken;
    private String accessToken;
    private Instant issuedAt;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;
}
