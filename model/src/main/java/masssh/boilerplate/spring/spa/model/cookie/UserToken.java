package masssh.boilerplate.spring.spa.model.cookie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken implements Serializable {
    private String userId;
    private String accessToken;
    private String role;
}
