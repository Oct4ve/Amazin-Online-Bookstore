package com.amazin.amazinonlinebookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class AccessingDatabase {

    private static final Logger log = LoggerFactory.getLogger(AccessingDatabase.class);


    public static void main(String[] args) {
        SpringApplication.run(AccessingDatabase.class, args);
    }

    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
        };
    }
}
