package shortener.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingRequestsInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(LoggingRequestsInterceptor.class);

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        requestWrapper.getParameterMap();
        String requestBody = new String(requestWrapper.getContentAsByteArray());

        logger.info(requestWrapper.getMethod() + " '" + requestWrapper.getRequestURI() + "' Req.body= " + requestBody);

        return true;
    }

    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
    }

}
