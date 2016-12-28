package eu.execom.representation.spring_mvc_integration.configuration;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages = "eu.execom.representation.spring_mvc_integration.repository")
@PropertySource({"classpath:jdbc.properties"})

public class DataConfig {

    @Autowired
    private Environment environment;

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        ds.setUrl(environment.getRequiredProperty("jdbc.url"));
        ds.setUsername(environment.getRequiredProperty("jdbc.username"));
        ds.setPassword(environment.getRequiredProperty("jdbc.password"));
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPersistenceProvider(new HibernatePersistenceProvider());
        emf.setPackagesToScan("eu.execom.labs.pomodoro.entity");
        emf.setJpaProperties(hibProperties());
        return emf;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

}
