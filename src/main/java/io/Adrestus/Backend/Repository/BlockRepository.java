package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.BlockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<BlockModel, Long> {
    BlockModel findByBlockhash(String BlockHash);

    @Modifying
    @Transactional
    @Query(value = "SELECT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.timestamp as creationDate, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, trx.nonce as nonce, trx.xaxis as xAxis, trx.yaxis as yAxis, trx.v as v, trx.r as r, trx.s as s, trx.pub as pub, br.blockhash as blockHash, br.height as blockHeight FROM user.transactions trx, user.blocks br WHERE br.blockhash=trx.block_hash AND br.blockhash LIKE ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByBlockHash(String hash);

    @Transactional
    @Query(value = "SELECT * FROM user.blocks ORDER BY blocks.timestamp DESC LIMIT 1;",nativeQuery = true)
    BlockModel findLatestAddedBlockByTimestamp();
}
