package com.amazin.amazinonlinebookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;

@SpringBootApplication
public class AccessingDatabase {

    private static final Logger log = LoggerFactory.getLogger(AccessingDatabase.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessingDatabase.class, args);
    }

    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
            Book book = new Book("Effective Java",  "Best practices for Java", "Joshua Bloch","8647823308954", new Date(124, 1, 1), 42.99);

            repository.save(book);

            log.info("Books found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(books -> {
                log.info(books.toString());
            });
            log.info("");
        };
    }
}
