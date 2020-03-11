package masssh.boilerplate.spring.spa.api.security;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.api.service.EnvironmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final EnvironmentService environmentService;
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;
    private final OpenIdUserService openIdUserService;
    private final TokenPreAuthenticationFilter tokenPreAuthenticationFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (environmentService.isLocal()) {
            http.authorizeRequests()
                    .antMatchers("/local/**").permitAll();
        }

        http.authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/api/login",
                        "/api/signUp",
                        "/favicon.ico",
                        "/oauth2/**",
                        "/actuator/**"
                ).permitAll()
                .anyRequest()
                .authenticated();

        // Authorization with access token in HTTP Header
        http.addFilter(tokenPreAuthenticationFilter);

        // Login API
        http.formLogin().disable();

        http.oauth2Login()
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(restAuthenticationFailureHandler)
                .permitAll()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .and()
                .userInfoEndpoint()
                .oidcUserService(openIdUserService);

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new RestEntryPoint(HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE);

//        final CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
//        csrfTokenRepository.setCookieHttpOnly(false);
//        http.csrf().csrfTokenRepository(csrfTokenRepository);
        http.csrf().disable();

        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
//        corsConfiguration.addAllowedOrigin(applicationProperty.getSecurity().getAllowOrigin());
        corsConfiguration.addAllowedOrigin("*");
        final UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        http.cors().configurationSource(corsConfigurationSource);
//        http.cors().disable();
    }

}
