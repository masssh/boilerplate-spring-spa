package masssh.boilerplate.spring.spa.api.config;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.api.security.CookiePreAuthenticationFilter;
import masssh.boilerplate.spring.spa.api.service.UserService;
import masssh.boilerplate.spring.spa.property.ApplicationProperty;
import masssh.boilerplate.spring.spa.property.ApplicationProperty.OAuth2ClientProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final ApplicationProperty applicationProperty;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(List.of(googleClientRegistration()));
    }

    /**
     * authorization endpoint is below:
     * /oauth2/authorization/google
     *
     * @return
     */
    private ClientRegistration googleClientRegistration() {
        final OAuth2ClientProperty clientProperty = applicationProperty.getOauth2Google();
        // /oauth2/authorization/google
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                       .clientId(clientProperty.getClientId())
                       .clientSecret(clientProperty.getClientSecret())
                       .scope(clientProperty.getScope().split(","))
                       .build();
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

    @Bean
    public CookiePreAuthenticationFilter requestHeaderAuthenticationFilter(final PreAuthenticatedAuthenticationProvider preAuthenticationProvider,
                                                                           final UserService userService) {
        CookiePreAuthenticationFilter filter = new CookiePreAuthenticationFilter(userService);
        filter.setAuthenticationManager(new ProviderManager(List.of(preAuthenticationProvider)));
        return filter;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticationProvider(final UserService userService) {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(userService);
        return provider;
    }
}
