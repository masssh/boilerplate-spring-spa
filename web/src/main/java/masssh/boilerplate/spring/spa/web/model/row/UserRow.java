package masssh.boilerplate.spring.spa.web.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@Data
@ToString(exclude = {"email", "passwordHash", "accessToken"})
@NoArgsConstructor
@AllArgsConstructor
public class UserRow implements Serializable {
    private OAuth2GoogleRow oAuth2GoogleRow;
    private String userId;
    private String userName;
    private String role;
    private String email;
    private String locale;
    private String passwordHash;
    private String accessToken;
    private String googleSubject;
}
