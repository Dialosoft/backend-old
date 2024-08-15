package com.dialosoft.postmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
public class PostManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostManagerApplication.class, args);
	}

}
