package com.matar.transaction.repositories;

import com.matar.transaction.model.trnsaction.Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String>,
        JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUserId(
            String id, Integer userId);

}
