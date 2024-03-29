package io.Adrestus.Backend.Service;


import io.Adrestus.Backend.DTO.CounterDetailsDTO;
import io.Adrestus.Backend.DTO.LimitBlockDetailsDTO;
import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Repository.BlockRepository;
import io.Adrestus.Backend.model.BlockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {
    @Autowired
    private BlockRepository blockRepository;

    public void save(BlockModel blockModel) {
        blockRepository.save(blockModel);
    }

    public void saveAll(List<BlockModel> blockModel) {
        blockRepository.saveAll(blockModel);
    }

    public BlockModel findByBlockhash(String BlockHash) {
        return blockRepository.findByBlockhash(BlockHash);
    }

    public LimitBlockDetailsDTO findLimitBlockDetailsDTOByHash(String hash) {
        return this.blockRepository.findLimitBlockDetailsDTOByHash(hash);
    }

    public List<LimitBlockDetailsDTO> findAllBlocksBetweenRange(int from, int to) {
        return this.blockRepository.findAllBlocksBetweenRange(from, to);
    }

    public List<TransactionDetailsDTO> findAllTransactionsByBlockHash(String hash) {
        return blockRepository.findAllTransactionsByBlockHash(hash);
    }

    public CounterDetailsDTO findNumberOfAllBlocks() {
        return this.blockRepository.findNumberOfAllBlocks();
    }

    public BlockModel findLatestAddedBlockByTimestamp() {
        return this.blockRepository.findLatestAddedBlockByTimestamp();
    }

    public CounterDetailsDTO findNumberOfTransactionsByBlockHash(String hash) {
        return this.blockRepository.findNumberOfTransactionsByBlockHash(hash);
    }
}
