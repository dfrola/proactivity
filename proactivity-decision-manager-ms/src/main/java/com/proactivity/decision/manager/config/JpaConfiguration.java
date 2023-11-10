package com.proactivity.decision.manager.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"com.proactivity.decision.manager.persistence.repository"}, entityManagerFactoryRef = "peopleEntityManagerFactory", transactionManagerRef = "peopleTransactionManager")
public class JpaConfiguration {
		
	@Bean
	@Primary
    @ConfigurationProperties("spring.datasource.dm")
    public DataSourceProperties peopleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.dm.hikari")
    public DataSource peopleDataSource() {
        return peopleDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public JdbcTemplate peopleJdbcTemplate(@Qualifier("peopleDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "peopleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("peopleDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.proactivity.decision.manager.persistence.entity").persistenceUnit("dm").build();
    }

    @Primary
    @Bean(name = "peopleTransactionManager")
    public PlatformTransactionManager peopleTransactionManager(@Qualifier("peopleEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }
}
