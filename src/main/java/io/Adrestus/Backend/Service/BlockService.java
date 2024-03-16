package io.Adrestus.Backend.Service;


import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Repository.BlockRepository;
import io.Adrestus.Backend.model.AccountModel;
import io.Adrestus.Backend.model.BlockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {
    @Autowired
    private BlockRepository blockRepository;

    public void save(BlockModel blockModel){
        blockRepository.save(blockModel);
    }

    public BlockModel findByBlockhash(String BlockHash) {
        return blockRepository.findByBlockhash(BlockHash);
    }

    public List<TransactionDetailsDTO> findAllTransactionsByBlockHash(String hash) {
        return blockRepository.findAllTransactionsByBlockHash(hash);
    }
}
