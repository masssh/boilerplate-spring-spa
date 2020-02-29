package masssh.boilerplate.spring.spa.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieService {
    public static Cookie findCookie(final Cookie[] cookies, final String name) {
        if (StringUtils.isEmpty(name) || cookies == null || cookies.length == 0) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(item -> Objects.equals(item.getName(), name))
                .findFirst()
                .orElse(null);
    }

    public String getToken(final HttpServletRequest request) {
        return Optional.ofNullable(findCookie(request.getCookies(), "token")).map(Cookie::getValue).orElse(null);
    }

    public void setToken(final HttpServletResponse response, final String value) {
        response.addCookie(new Cookie("token", value));
    }
}
