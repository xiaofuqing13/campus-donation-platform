package com.donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.donation")
@EntityScan("com.donation.model")
@EnableJpaRepositories("com.donation.repository")
public class Demo3Application {
    public static void main(String[] args) {
        SpringApplication.run(Demo3Application.class, args);
    }
} 