package masssh.boilerplate.spring.spa.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequiredArgsConstructor
@Slf4j
public class WebController {
    @GetMapping({"/web", "/web/", "/web/**"})
    public ModelAndView index() {
        final InternalResourceView view = new InternalResourceView("/static/index.html");
        return new ModelAndView(view);
    }

    @GetMapping({"", "/"})
    public View redirect() {
        return new RedirectView("/web");
    }
}
