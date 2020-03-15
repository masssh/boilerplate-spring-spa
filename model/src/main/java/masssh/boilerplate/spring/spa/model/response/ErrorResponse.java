package masssh.boilerplate.spring.spa.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public final class ErrorResponse extends BaseResponse {
    public static ErrorResponse notFound() {
        return new ErrorResponse(HttpStatus.NOT_FOUND);
    }

    public ErrorResponse(final HttpStatus httpStatus) {
        status = httpStatus.value();
        message = httpStatus.getReasonPhrase();
    }

    public ErrorResponse(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
