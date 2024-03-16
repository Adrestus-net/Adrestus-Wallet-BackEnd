package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.BlockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<BlockModel, Long> {
    BlockModel findByBlockhash(String BlockHash);

    @Query(value = "SELECT trx.transactionhash as transaction_hash,trx.to as toAddress,trx.from as fromAddress,trx.block_hash as block_hash FROM user.transactions trx, user.blocks WHERE blocks.blockhash=trx.block_hash AND blocks.blockhash LIKE ?1", nativeQuery = true)
    List<TransactionDetailsDTO> findAllTransactionsByBlockHash(String hash);
}
