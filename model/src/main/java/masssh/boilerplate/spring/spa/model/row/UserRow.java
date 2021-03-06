package masssh.boilerplate.spring.spa.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Data
@ToString(exclude = {"email", "passwordHash", "accessToken"})
@NoArgsConstructor
@AllArgsConstructor
public class UserRow implements Serializable {
    private OAuth2GoogleRow oAuth2GoogleRow;
    private long userId;
    private String userHash;
    private String userName;
    private String role;
    private String email;
    private String locale;
    private String passwordHash;
    private String accessToken;
    private String googleSubject;
    private Instant createdAt;
    private Instant updatedAt;
}
