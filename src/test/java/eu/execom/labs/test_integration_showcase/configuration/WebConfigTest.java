package eu.execom.labs.test_integration_showcase.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import eu.execom.labs.test_integration_showcase.controller.ControllerComponents;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = ControllerComponents.class)
public class WebConfigTest extends WebMvcConfigurerAdapter {
}
