package masssh.boilerplate.spring.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    @GetMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
}
