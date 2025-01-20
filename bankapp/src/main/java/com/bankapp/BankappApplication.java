package com.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.bankapp.repository")
@EntityScan(basePackages = "com.bankapp.model")
public class BankappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankappApplication.class, args);
    }
}