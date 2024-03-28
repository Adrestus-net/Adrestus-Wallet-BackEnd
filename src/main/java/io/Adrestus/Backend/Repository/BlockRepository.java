package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.CounterDetailsDTO;
import io.Adrestus.Backend.DTO.LimitBlockDetailsDTO;
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

    @Query(value = "SELECT DISTINCT COUNT(br.blockhash) as trxcounter FROM user.blocks br", nativeQuery = true)
    CounterDetailsDTO findNumberOfAllBlocks();

    @Query(value =
            "SELECT br.zone as zone, br.blockhash as hash, br.height as height, br.timestamp as timestamp, br.transaction_proposer as miner, br.merkle_root as merkleRoot, count(trx.transactionhash) as transactions\n" +
                    "FROM user.transactions trx, user.blocks br\n" +
                    "WHERE trx.block_hash=br.blockhash\n" +
                    "GROUP BY br.blockhash\n" +
                    "ORDER BY br.timestamp DESC\n" +
                    "LIMIT ?1, ?2", nativeQuery = true)
    List<LimitBlockDetailsDTO> findAllBlocksBetweenRange(int from, int to);

    @Modifying
    @Transactional
    @Query(value = "SELECT trx.transactionhash as transaction_hash, trx.transaction_type as transactionType, trx.status as statusType, trx.zone_from as zoneFrom, trx.zone_to as zoneTo, trx.timestamp as creationDate, trx.from as fromAddress, trx.to as toAddress, trx.amount as amount, trx.amount_with_transaction_fee as amountWithTransactionFee, trx.nonce as nonce, trx.xaxis as xAxis, trx.yaxis as yAxis, trx.v as v, trx.r as r, trx.s as s, trx.pub as pub, br.blockhash as blockHash, br.height as blockHeight FROM user.transactions trx, user.blocks br WHERE br.blockhash=trx.block_hash AND br.blockhash LIKE ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByBlockHash(String hash);

    @Transactional
    @Query(value = "SELECT * FROM user.blocks ORDER BY blocks.timestamp DESC LIMIT 1;", nativeQuery = true)
    BlockModel findLatestAddedBlockByTimestamp();

    @Query(value = "SELECT COUNT(trx.transactionhash) as trxcounter FROM user.transactions trx, user.blocks WHERE blocks.blockhash=trx.block_hash AND blocks.blockhash = ?1", nativeQuery = true)
    CounterDetailsDTO findNumberOfTransactionsByBlockHash(String hash);
}
