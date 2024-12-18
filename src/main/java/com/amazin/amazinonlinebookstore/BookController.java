// This controller controls everything in the database.
// I don't think we need this at all

package com.amazin.amazinonlinebookstore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/{id}")
    public Book addBook(@RequestBody Book newBook){
        return bookRepository.save(newBook);
    }

    @DeleteMapping("/{id}")
    public void removeBook(@PathVariable Long id){
        Book deleteBook = bookRepository.findById(id).orElseThrow();
        bookRepository.delete(deleteBook);
    }
}
