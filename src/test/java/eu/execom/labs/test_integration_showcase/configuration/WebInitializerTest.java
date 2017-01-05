package eu.execom.labs.test_integration_showcase.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializerTest extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootConfigTest.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {WebConfigTest.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
