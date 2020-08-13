package shortener.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    // Этот метод должен содержать конфигурации которые инициализируют Beans
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SecurityConfiguration.class, WebMvcConfig.class, AppConfig.class };
    }

    // Тут добавляем конфигурацию, в которой инициализируем ViewResolver
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebMvcConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
