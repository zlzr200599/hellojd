package com.example.hellojd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HellojdApplication {
    public static void main(String[] args) {
        SpringApplication.run(HellojdApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Command line");
            System.out.println("The program running");

        };
    }
}
