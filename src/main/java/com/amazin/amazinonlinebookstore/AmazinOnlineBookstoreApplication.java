package com.amazin.amazinonlinebookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AmazinOnlineBookstoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmazinOnlineBookstoreApplication.class, args);
    }

}
