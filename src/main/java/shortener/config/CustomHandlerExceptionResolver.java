package shortener.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@ControllerAdvice
public class CustomHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    private static final Logger logger = LogManager.getLogger(CustomHandlerExceptionResolver.class);

    @ExceptionHandler(value= HttpRequestMethodNotSupportedException.class)
    @Override
    protected ModelAndView handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request,
            HttpServletResponse response, @Nullable Object handler)
            throws IOException {

        String path = null;
        try {
            path = new File(WebMvcConfig.class.getProtectionDomain().
                    getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            logger.error(e, e.getCause());
        }

        response.setHeader("Content-Type", "text/html;charset=UTF-8");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!CustomHandlerExceptionResolver");

        if (path != null) {
            Path pathObj = Path.of(path + "/templates/index.html");
            String content = Files.readString(pathObj, StandardCharsets.UTF_8);
            return new ModelAndView(new HTMLView(content));
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return new ModelAndView();
    }
}
