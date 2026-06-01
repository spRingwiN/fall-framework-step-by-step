package com.eric.fall.app.jdbc.jdbc;

import com.eric.fall.app.jdbc.annotation.Autowired;
import com.eric.fall.app.jdbc.annotation.Bean;
import com.eric.fall.app.jdbc.annotation.Configuration;
import com.eric.fall.app.jdbc.annotation.Value;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

    @Bean(destroyMethod = "close")
    DataSource dataSource(
            // properties:
            @Value("${fall.datasource.url}") String url,
            @Value("${fall.datasource.username}") String username,
            @Value("${fall.datasource.password}") String password,
            @Value("${fall.datasource.driver-class-name}") String driver,
            @Value("${fall.datasource.maximum-pool-size:20}") int maxPoolSize,
            @Value("${fall.datasource.minimum-pool-size:1}") int minPoolSize,
            @Value("${fall.datasource.connection-timeout:30000}") int connTimeout
    ) {
        var config = new HikariConfig();
        config.setAutoCommit(false);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        if (driver != null) {
            config.setDriverClassName(driver);
        }
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minPoolSize);
        config.setConnectionTimeout(connTimeout);
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
