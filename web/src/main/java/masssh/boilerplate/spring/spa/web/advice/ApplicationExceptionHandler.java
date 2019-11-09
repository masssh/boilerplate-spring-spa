package masssh.boilerplate.spring.spa.web.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import masssh.boilerplate.spring.spa.model.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleOtherException(Exception exception, WebRequest request) {
        final HttpStatus status;
        final int code;
        final String message;

        if (exception instanceof ResponseStatusException) {
            throw (ResponseStatusException) exception;
        } else if (exception instanceof IllegalArgumentException) {
            status = BAD_REQUEST;
            code = status.value();
            message = StringUtils.isEmpty(exception.getMessage())
                    ? BAD_REQUEST.getReasonPhrase()
                    : exception.getMessage();
        } else if (exception instanceof MethodArgumentNotValidException) {
            final BindingResult result = ((MethodArgumentNotValidException) exception).getBindingResult();
            status = BAD_REQUEST;
            code = status.value();
            message = result.toString();
        } else {
            status = INTERNAL_SERVER_ERROR;
            code = status.value();
            message = exception.getMessage();
        }

        final HttpHeaders headers = new HttpHeaders();
        final ErrorResponse body = new ErrorResponse(code, message);
        return handleExceptionInternal(exception, body, headers, status, request);
    }
}
