package com.strugglingcoder.springjpaexplorer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by Kini on 31-Jan-18.
 */

@EnableJpaRepositories
@PropertySource("classpath:database.properties")
@Configuration //because we have declared @Bean in this file. Any classes that have @Bean needs to have @Configuration
public class JPAConfig {

    private static final String DRIVER = "db.driver";
    private static final String URL = "db.url";
    private static final String USERNAME="db.username";
    private static final String PASSWORD="db.password";

    @Autowired
    Environment env;

    @Bean
    public DriverManagerDataSource driverManagerDataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getRequiredProperty(DRIVER));
        driverManagerDataSource.setUrl(env.getRequiredProperty(URL));
        driverManagerDataSource.setUsername(env.getRequiredProperty(USERNAME));
        driverManagerDataSource.setPassword(env.getRequiredProperty(PASSWORD));
        return driverManagerDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){

        LocalContainerEntityManagerFactoryBean localEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localEntityManagerFactoryBean.setDataSource(driverManagerDataSource());
        localEntityManagerFactoryBean.setPackagesToScan("com.strugglingcoder.springjpaexplorer.entity");

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true); // we want to see the intermediate sql, the whole point of the project!
        jpaVendorAdapter.setGenerateDdl(true);

        localEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        return localEntityManagerFactoryBean;

    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }

}
