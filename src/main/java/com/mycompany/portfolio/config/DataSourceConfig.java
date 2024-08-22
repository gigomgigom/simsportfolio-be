package com.mycompany.portfolio.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class DataSourceConfig {
	
	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/sim-portfolio");
		config.setUsername("root");
		config.setPassword("1234");
		config.setMaximumPoolSize(12);
		HikariDataSource hikariDataSource = new HikariDataSource(config);
		return hikariDataSource;
	}
}
