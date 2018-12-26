package com.vtyc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Value("${spring.datasource.driverClassName}")
    private String primaryDriverClassName;

    @Value("${spring.datasource.url}")
    private String primaryUrl;

    @Value("${spring.datasource.username}")
    private String primaryUsername;

    @Value("${spring.datasource.password}")
    private String primaryPassword;

    @Value("${spring.datasource1.driverClassName}")
    private String secondaryDriverClassName;

    @Value("${spring.datasource1.url}")
    private String secondaryUrl;

    @Value("${spring.datasource1.username}")
    private String secondaryUsername;

    @Value("${spring.datasource1.password}")
    private String secondaryPassword;

    @Bean(name="primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource(){
        //return DataSourceBuilder.create().build();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(primaryDriverClassName);
        dataSource.setUrl(primaryUrl);
        dataSource.setUsername(primaryUsername);
        dataSource.setPassword(primaryPassword);

        return dataSource;
    }

    @Bean(name="secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource1")
    public DataSource secondaryDataSource(){
       //return DataSourceBuilder.create().build();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(secondaryDriverClassName);
        dataSource.setUrl(secondaryUrl);
        dataSource.setUsername(secondaryUsername);
        dataSource.setPassword(secondaryPassword);

        return dataSource;

    }

    @Bean(name = "jdbcPrimaryTemplate")
    @Autowired
    public JdbcTemplate jdbcPrimaryTemplate(@Qualifier(value = "primaryDataSource") DataSource primaryDataSource) {
        return new JdbcTemplate(primaryDataSource);
    }

    @Bean(name = "jdbcSecondaryTemplate")
    @Autowired
    public JdbcTemplate jdbcSecondaryTemplate(@Qualifier(value = "secondaryDataSource") DataSource secondaryDataSource) {
        return new JdbcTemplate(secondaryDataSource);
    }
}
