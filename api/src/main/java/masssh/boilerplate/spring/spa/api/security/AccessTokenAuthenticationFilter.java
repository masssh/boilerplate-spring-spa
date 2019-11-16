package masssh.boilerplate.spring.spa.api.security;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;

public class AccessTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        final String id = request.getHeader("X-Access-Id");
        return StringUtils.isEmpty(id) ? null : id;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        final String token = request.getHeader("X-Access-Token");
        return StringUtils.isEmpty(token) ? null : token;
    }

}
