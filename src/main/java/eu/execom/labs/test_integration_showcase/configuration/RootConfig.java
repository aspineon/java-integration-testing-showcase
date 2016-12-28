package eu.execom.labs.test_integration_showcase.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import eu.execom.labs.test_integration_showcase.service.ServiceComponents;

@Configuration
@Import({DataConfig.class})
@ComponentScan(basePackageClasses = ServiceComponents.class)
public class RootConfig {
}
