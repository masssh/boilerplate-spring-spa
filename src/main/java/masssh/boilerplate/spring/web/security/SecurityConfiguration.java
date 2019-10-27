package masssh.boilerplate.spring.web.security;

import static masssh.boilerplate.spring.web.security.Roles.ROLE_ADMIN;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.web.service.EnvironmentService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final EnvironmentService environmentService;
    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        if (environmentService.isLocal()) {
            http.authorizeRequests()
                    .antMatchers("/local/**").permitAll()
                    .antMatchers("/sample*").permitAll(); // !!! SHOULD BE DELETED !!!
        }

        http.authorizeRequests()
                .antMatchers("/", "/login", "/static/**", "/actuator/**").permitAll()
                .antMatchers("/api/admin/**").hasRole(ROLE_ADMIN)
                .anyRequest().authenticated();

        http.formLogin()
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler());

        http.logout();

    }
}
