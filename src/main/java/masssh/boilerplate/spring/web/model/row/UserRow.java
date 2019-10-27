package masssh.boilerplate.spring.web.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRow {
    private String userName;
    private String userToken;
    private String role;
}
