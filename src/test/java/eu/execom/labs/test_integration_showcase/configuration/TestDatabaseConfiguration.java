package eu.execom.labs.test_integration_showcase.configuration;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import eu.execom.labs.test_integration_showcase.entity.EntityComponents;
import eu.execom.labs.test_integration_showcase.repository.RepositoryComponents;

@Configuration
@EnableJpaRepositories(basePackageClasses = RepositoryComponents.class)
@PropertySource({"classpath:jdbc.test.properties"})
public class TestDatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.H2)
                                     .setName("person")
                                     .addScript("db/sql/create-db.sql")
                                     .build();
        return db;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPersistenceProvider(new HibernatePersistenceProvider());
        emf.setPackagesToScan(EntityComponents.ENTITY_PACKAGE);
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }
}
