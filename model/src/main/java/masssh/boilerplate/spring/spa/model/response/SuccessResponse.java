package masssh.boilerplate.spring.spa.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static org.springframework.http.HttpStatus.OK;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class SuccessResponse extends BaseResponse {
    public SuccessResponse() {
        status = OK.value();
        message = OK.getReasonPhrase();
    }
}
