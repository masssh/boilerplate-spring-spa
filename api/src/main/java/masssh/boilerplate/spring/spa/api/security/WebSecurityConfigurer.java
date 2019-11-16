package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.api.service.EnvironmentService;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final ApplicationProperty applicationProperty;
    private final EnvironmentService environmentService;
    private final RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter;
    private final PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;
    private final OpenIdUserService openIdUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserDetailsService);
        auth.authenticationProvider(preAuthenticatedAuthenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilter(requestHeaderAuthenticationFilter);

        if (environmentService.isLocal()) {
            http.authorizeRequests()
                    .antMatchers("/local/**").permitAll();
        }
        http.authorizeRequests()
                .antMatchers("/", "/login", "/favicon.ico", "/oauth2/**", "/actuator/**").permitAll()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .loginProcessingUrl("/api/login")
                .usernameParameter("userId")
                .passwordParameter("password")
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(restAuthenticationFailureHandler);

        http.oauth2Login()
                .loginPage("/login")
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(restAuthenticationFailureHandler)
                .permitAll()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .and()
                .userInfoEndpoint()
                .oidcUserService(openIdUserService);

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new RestEntryPoint(HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE);

        final CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();

        csrfTokenRepository.setCookieHttpOnly(false);
        http.csrf().csrfTokenRepository(csrfTokenRepository);

        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedOrigin(applicationProperty.getSecurity().getAllowOrigin());
        final UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        http.cors().configurationSource(corsConfigurationSource);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
