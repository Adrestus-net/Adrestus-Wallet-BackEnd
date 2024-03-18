package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    TransactionModel findByTransactionhash(String transactionHash);

    @Query(value = "SELECT DISTINCT trx.transactionhash as transaction_hash,trx.to as toAddress,trx.from as fromAddress,trx.block_hash as block_hash FROM user.transactions trx, user.accounts WHERE accounts.Address=trx.from AND accounts.Address= ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByFromAddress(String from);

    @Query(value = "SELECT DISTINCT trx.transactionhash as transaction_hash,trx.to as toAddress,trx.from as fromAddress,trx.block_hash as block_hash FROM user.transactions trx, user.accounts WHERE accounts.Address=trx.to AND accounts.Address= ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByToAddress(String to);
}
