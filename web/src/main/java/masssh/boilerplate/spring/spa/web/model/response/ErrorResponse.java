package masssh.boilerplate.spring.spa.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public final class ErrorResponse extends BaseResponse {
    private final int status;
    private final String message;
}
