package masssh.boilerplate.spring.web.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.web.config.property.ApplicationProperty;
import masssh.boilerplate.spring.web.service.EnvironmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final ApplicationProperty applicationProperty;
    private final EnvironmentService environmentService;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final OpenIdUserService openIdUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserDetailsService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (environmentService.isLocal()) {
            http.authorizeRequests()
                    .antMatchers("/local/**").permitAll()
                    .antMatchers("/sample*").permitAll(); // !!! SHOULD BE DELETED !!!
        }
        http.authorizeRequests()
                .antMatchers("/", "/test", "/login", "/error", "/oauth2/**", "/static/**", "/actuator/**").permitAll()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler());

        http.oauth2Login()
                .loginPage("/oauth2/login")
                .defaultSuccessUrl("/")
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .permitAll()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .and()
                .userInfoEndpoint()
                .oidcUserService(openIdUserService);

        http.logout().permitAll();

        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"))
                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), AnyRequestMatcher.INSTANCE);

        final CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieHttpOnly(false);
        http.csrf().csrfTokenRepository(csrfTokenRepository);

        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedOrigin(applicationProperty.getCors().getOrigin());
        final UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        http.cors().configurationSource(corsConfigurationSource);
    }

}
