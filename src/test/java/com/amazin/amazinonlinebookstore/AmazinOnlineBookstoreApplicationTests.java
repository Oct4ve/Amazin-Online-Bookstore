package com.amazin.amazinonlinebookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AmazinOnlineBookstoreApplicationTests {

    @Autowired
    private BookController bookController;
    @Autowired
    private ThymeLeafController thymeLeafController;

    @Test
    void contextLoads() throws Exception {
        assertThat(bookController).isNotNull();
        assertThat(thymeLeafController).isNotNull();
    }

}
