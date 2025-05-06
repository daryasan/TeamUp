package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.security")
public class ChatsApp {
    public static void main(String[] args) {
        SpringApplication.run(ChatsApp.class, args);
    }
}
