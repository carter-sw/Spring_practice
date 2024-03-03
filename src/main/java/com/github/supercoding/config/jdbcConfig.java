package com.github.supercoding.config;

import com.github.supercoding.properties.DataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@RequiredArgsConstructor
public class jdbcConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource1(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl()); // 접근이 안될 시 ?useUnicode=true&characterEncoding=UTF-8 추가

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
