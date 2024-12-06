package com.amazin.amazinonlinebookstore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<PreviousPurchase, Long> {
    PreviousPurchase findByid(Long id);
    PreviousPurchase findByUser(User user);
    List<PreviousPurchase> findAllByUser(User user);
}
