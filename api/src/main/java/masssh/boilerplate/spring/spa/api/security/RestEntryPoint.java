package masssh.boilerplate.spring.spa.api.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class RestEntryPoint implements AuthenticationEntryPoint {
    private final HttpStatus httpStatus;

    /**
     * Creates a new instance.
     *
     * @param httpStatus the HttpStatus to set
     */
    public RestEntryPoint(HttpStatus httpStatus) {
        Assert.notNull(httpStatus, "httpStatus cannot be null");
        this.httpStatus = httpStatus;
    }

    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
    }
}
