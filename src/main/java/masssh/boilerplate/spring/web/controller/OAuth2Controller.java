package masssh.boilerplate.spring.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {
    private static final String authorizationRequestBaseUri = "/oauth2/authorization";
    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/oauth2/login")
    public ModelAndView oauth2Login() {
        final ModelAndView mav = new ModelAndView();
        Iterable<ClientRegistration> clientRegistrations = new ArrayList<>();
        final ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            //noinspection unchecked
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        final Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        mav.addObject("urls", oauth2AuthenticationUrls);
        mav.setViewName("oauth2_login");
        return mav;
    }

    @GetMapping("/oauth2/login/success")
    public String oauth2Redirect(final Principal principal) {
        log.info("principal: {}", principal);
        return "redirect:/";
    }
}
