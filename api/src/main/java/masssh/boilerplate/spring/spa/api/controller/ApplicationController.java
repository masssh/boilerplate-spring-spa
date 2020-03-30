package masssh.boilerplate.spring.spa.api.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        final HttpStatus status = getStatus(request);
        throw new ResponseStatusException(status);
    }

    @RequestMapping
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        final HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        throw new ResponseStatusException(status);
    }
}
