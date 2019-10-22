package masssh.boilerplate.spring.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthenticationHandler authenticationHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/static/**/*.html", "/static/**/*.htm").denyAll()
                .antMatchers("/", "/sample*", "/error", "/login", "/static/**", "/actuator/**").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/")
                .successHandler(authenticationHandler)
                .failureHandler(authenticationHandler);
        http.csrf().ignoringAntMatchers("/actuator/**", "/sample*");
    }
}
