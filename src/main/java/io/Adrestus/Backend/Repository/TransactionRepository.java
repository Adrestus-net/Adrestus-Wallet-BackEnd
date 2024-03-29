package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.CounterDetailsDTO;
import io.Adrestus.Backend.DTO.LimitTransactionsDetailsDTO;
import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    TransactionModel findByTransactionhash(String transactionHash);

    @Query(value =
            "SELECT DISTINCT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.timestamp as creationDate,  trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, br.height as blockHeight " +
                    "FROM user.transactions trx, user.blocks br\n" +
                    "WHERE trx.block_hash=br.blockhash AND trx.transactionhash IN :hashes", nativeQuery = true)
    List<LimitTransactionsDetailsDTO> findLimitTransactionsDetailsByTransactionHash(@Param("hashes") List<String> hashes);
    @Query(value = "SELECT DISTINCT COUNT(trx.transactionhash) as trxcounter FROM user.transactions trx", nativeQuery = true)
    CounterDetailsDTO findNumberOfAllTransactions();

    @Query(value =
            "SELECT  DISTINCT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.timestamp as creationDate,  trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, br.height as blockHeight\n" +
                    "FROM user.transactions trx, user.blocks br\n" +
                    "WHERE trx.block_hash=br.blockhash\n" +
                    "ORDER BY trx.timestamp DESC\n" +
                    "LIMIT ?1, ?2", nativeQuery = true)
    List<LimitTransactionsDetailsDTO> findAllTransactionsBetweenRange(int from, int to);

    @Modifying
    @Transactional
    @Query(value = "SELECT DISTINCT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.timestamp as creationDate, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, trx.nonce as nonce, trx.xaxis as xAxis, trx.yaxis as yAxis, trx.v as v, trx.r as r, trx.s as s, trx.pub as pub, br.blockhash as blockHash, br.height as blockHeight  FROM user.transactions trx, user.accounts, user.blocks br WHERE accounts.Address=trx.from AND br.blockhash=trx.block_hash AND accounts.Address= ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByFromAddress(String from);

    @Modifying
    @Transactional
    @Query(value = "SELECT DISTINCT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.timestamp as creationDate, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, trx.nonce as nonce, trx.xaxis as xAxis, trx.yaxis as yAxis, trx.v as v, trx.r as r, trx.s as s, trx.pub as pub, br.blockhash as blockHash, br.height as blockHeight FROM user.transactions trx, user.accounts, user.blocks br WHERE accounts.Address=trx.to AND br.blockhash=trx.block_hash AND accounts.Address= ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByToAddress(String to);
}
