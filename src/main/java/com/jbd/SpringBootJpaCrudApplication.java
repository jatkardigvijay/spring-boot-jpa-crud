package com.jbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.jbd.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SpringBootJpaCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaCrudApplication.class, args);
	}

}
