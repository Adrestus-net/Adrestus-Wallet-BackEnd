package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.CounterDetailsDTO;
import io.Adrestus.Backend.DTO.LimitTransactionsDetailsDTO;
import io.Adrestus.Backend.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    AccountModel findByAddress(String address);

    @Query(value = "SELECT * FROM user.accounts u", nativeQuery = true)
    List<AccountModel> findAllActiveAccounts();

    @Query(value = "SELECT * FROM user.accounts WHERE accounts.Address= ?1", nativeQuery = true)
    AccountModel findAccountByAddress(String address);

    @Query(value = "SELECT * FROM user.accounts WHERE accounts.Address IN :addresses", nativeQuery = true)
    List<AccountModel> findAccountByListOfAddresses(@Param("addresses") List<String> addresses);

    @Query(value = "SELECT * FROM user.accounts ORDER BY accounts.account_id DESC LIMIT 1", nativeQuery = true)
    AccountModel findLastAccountEntry();

    @Query(value = "SELECT DISTINCT COUNT(trx.transactionhash) as trxcounter\n" +
            "FROM user.transactions trx, user.accounts\n" +
            "WHERE accounts.address=trx.from AND accounts.address= ?1 \n" +
            "OR trx.transactionhash IN(SELECT DISTINCT(trx.transactionhash) \n" +
            "                           FROM user.transactions trx, user.accounts\n" +
            "\t\t\t\t\t\t   WHERE accounts.address=trx.to AND accounts.address= ?1)", nativeQuery = true)
    CounterDetailsDTO findNumberOfTransactionsByAccountAddress(String address);


    @Query(value = "" +
            "SELECT DISTINCT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.timestamp as creationDate, trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, br.height as blockHeight\n" +
            "FROM user.transactions trx, user.accounts, user.blocks br\n" +
            "WHERE accounts.address=trx.from AND trx.block_hash=br.blockhash AND accounts.address= ?1 \n" +
            "OR trx.transactionhash IN(SELECT DISTINCT(trx.transactionhash) \n" +
            "                           FROM user.transactions trx, user.accounts\n" +
            "\t\t\t\t\t\t   WHERE accounts.address=trx.to AND accounts.address= ?1)\n" +
            "ORDER BY trx.timestamp DESC\n" +
            "LIMIT ?2,?3", nativeQuery = true)
    List<LimitTransactionsDetailsDTO> findTransactionByAccountAddressInRange(String address, int from, int to);
}
