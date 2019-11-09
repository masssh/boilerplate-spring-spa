package masssh.boilerplate.spring.spa.web.model.request;

import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString(exclude = {"password"})
public final class SampleRequest {
    private Integer foo;
    @NotNull
    @Size(max = 3)
    private String bar;
    private String password;
}
