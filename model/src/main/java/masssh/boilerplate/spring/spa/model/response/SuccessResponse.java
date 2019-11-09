package masssh.boilerplate.spring.spa.model.response;

import static org.springframework.http.HttpStatus.OK;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public final class SuccessResponse extends BaseResponse {
    private final int status;
    private final String message;

    public SuccessResponse() {
        status = OK.value();
        message = OK.getReasonPhrase();
    }
}
