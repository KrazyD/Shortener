package shortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"shortener"})
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingRequestsInterceptor loggingRequestsInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/src/main/resources/**",
                                                "/main/src/main/resources/**" )
                .addResourceLocations("WEB-INF/classes/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingRequestsInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new CustomHandlerExceptionResolver());
    }

    @Bean
    public AbstractController defaultController() {
        return new AbstractController() {
            @Override
            protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest,
                                                         HttpServletResponse httpServletResponse) throws Exception {
                String path = new File(WebMvcConfig.class.getProtectionDomain().
                                    getCodeSource().getLocation().toURI()).getPath();
                Path pathObj = Path.of(path + "/templates/index.html");
                String content = Files.readString(pathObj, StandardCharsets.UTF_8);
                httpServletResponse.setHeader("Content-Type", "text/html;charset=UTF-8");
                return new ModelAndView(new HTMLView(content));
            }
        };
    }

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping
                = new SimpleUrlHandlerMapping();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("/**", defaultController());
        simpleUrlHandlerMapping.setUrlMap(urlMap);
        simpleUrlHandlerMapping.setOrder(Ordered.LOWEST_PRECEDENCE);
        return simpleUrlHandlerMapping;
    }
}