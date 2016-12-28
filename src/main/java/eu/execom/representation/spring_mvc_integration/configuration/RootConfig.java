package eu.execom.representation.spring_mvc_integration.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataConfig.class})
@ComponentScan(basePackages = "eu.execom.representation.spring_mvc_integration.service")
public class RootConfig {
}
