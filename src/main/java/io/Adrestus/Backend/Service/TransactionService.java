package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.Repository.TransactionRepository;
import io.Adrestus.Backend.model.BlockModel;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public void save(TransactionModel transactionModel){
        transactionRepository.save(transactionModel);
    }

    public TransactionModel findByTransactionhash(String transactionHash){
        TransactionModel d= this.transactionRepository.findByTransactionhash(transactionHash);
        return this.transactionRepository.findByTransactionhash(transactionHash);
    }

}
