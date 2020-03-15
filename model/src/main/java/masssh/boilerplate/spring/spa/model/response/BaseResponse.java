package masssh.boilerplate.spring.spa.model.response;

import lombok.Data;

@Data
abstract public class BaseResponse {
    int status;
    String message;
}
