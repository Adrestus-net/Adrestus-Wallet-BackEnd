package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    TransactionModel findByTransactionhash(String transactionHash);
}
