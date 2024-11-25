package com.amazin.amazinonlinebookstore;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<ShoppingCart, Long> {
    ShoppingCart findByid(Long id);
}
