package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.AccountModel;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {

    AccountModel findByAddress(String address);

    @Query(value = "SELECT trx.transactionhash as transaction_hash,trx.to as toAddress,trx.from as fromAddress,trx.block_hash as block_hash FROM user.transactions trx, user.accounts WHERE accounts.Address=trx.from AND accounts.Address LIKE ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByFromAddress(String from);

    @Query(value = "SELECT trx.transactionhash as transaction_hash,trx.to as toAddress,trx.from as fromAddress,trx.block_hash as block_hash FROM user.transactions trx, user.accounts WHERE accounts.Address=trx.to AND accounts.Address LIKE ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByToAddress(String to);
    @Modifying
    @Transactional
    @Query(value = "UPDATE user.accounts SET balance = ? WHERE accounts.Address = ?;", nativeQuery = true)
    int updateAccountSetBalanceForAddress(double balance, String address);
}
