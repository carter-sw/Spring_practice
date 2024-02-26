package com.github.supercoding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class jdbcConfig {

    @Bean
    public DataSource dataSource1(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("12341234");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/chapter_96"); // 접근이 안될 시 ?useUnicode=true&characterEncoding=UTF-8 추가

        return dataSource;

    }

    @Bean
    public DataSource dataSource2(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("12341234");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/chapter_97"); // 접근이 안될 시 ?useUnicode=true&characterEncoding=UTF-8 추가

        return dataSource;

    }

    @Bean
    public JdbcTemplate jdbcTemplate1(){
        return new JdbcTemplate(dataSource1());
    }

    @Bean
    public JdbcTemplate jdbcTemplate2(){
        return new JdbcTemplate(dataSource2());
    }


    @Bean(name ="tm1")
    public PlatformTransactionManager transactionManager1(){return new DataSourceTransactionManager(dataSource1());
    }

    @Bean(name ="tm2")
    public PlatformTransactionManager transactionManager2(){return new DataSourceTransactionManager(dataSource2());
    }
}
