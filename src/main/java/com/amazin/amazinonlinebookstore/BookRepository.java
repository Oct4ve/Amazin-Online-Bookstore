package com.amazin.amazinonlinebookstore;
import org.springframework.data.repository.CrudRepository;

public interface  BookRepository extends CrudRepository<Book, Long> {
    Book findByTitle(String title);
    Book findByid(Long id);
}
