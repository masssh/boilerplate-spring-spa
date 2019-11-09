package masssh.boilerplate.spring.spa.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@EnableConfigurationProperties({ServerProperties.class})
@Slf4j
public class ApplicationController extends BasicErrorController {
    @Autowired
    public ApplicationController(ServerProperties serverProperties, ErrorAttributes errorAttributes, ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(),
                errorViewResolvers.orderedStream().collect(Collectors.toList()));
    }

    @GetMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        final HttpStatus status = getStatus(request);
        final ModelAndView mv = new ModelAndView();
        if (status == HttpStatus.NOT_FOUND) {
            response.setStatus(HttpServletResponse.SC_OK);
            mv.setViewName("index");
            return mv;
        }
        mv.setView(new RedirectView("/"));
        return mv;
    }

    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        return new ResponseEntity<>(body, status);
    }
}
