package com.isp.bonifico.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"com.isp.bonifico.persistence.repository"}, entityManagerFactoryRef = "bonificoEntityManagerFactory", transactionManagerRef = "bonificoTransactionManager")
public class Config {

	@Bean	
    @ConfigurationProperties("spring.datasource.bonifico")
    public DataSourceProperties bonificoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean   
    @ConfigurationProperties("spring.datasource.bonifico.hikari")
    public DataSource bonificoDataSource() {
        return bonificoDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean   
    public JdbcTemplate bonificoJdbcTemplate(@Qualifier("bonificoDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
   
    @Bean(name = "bonificoEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean bonificoEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("bonificoDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.isp.bonifico.persistence.entity").persistenceUnit("bonifico").build();
    }

    @Bean(name = "bonificoTransactionManager")
    public PlatformTransactionManager bonificoTransactionManager(@Qualifier("bonificoEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }
}
