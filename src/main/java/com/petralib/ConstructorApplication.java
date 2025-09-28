package com.petralib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.petralib")
public class ConstructorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConstructorApplication.class, args);
    }

}
