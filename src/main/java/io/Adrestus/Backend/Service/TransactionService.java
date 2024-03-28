package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.DTO.CounterDetailsDTO;
import io.Adrestus.Backend.DTO.LimitTransactionsDetailsDTO;
import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Repository.TransactionRepository;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public void save(TransactionModel transactionModel) {
        transactionRepository.save(transactionModel);
    }

    public void saveAll(List<TransactionModel> transactionModel) {
        transactionRepository.saveAll(transactionModel);
    }

    public TransactionModel findByTransactionhash(String transactionHash) {
        return this.transactionRepository.findByTransactionhash(transactionHash);
    }

    public List<LimitTransactionsDetailsDTO> findAllTransactionsBetweenRange(int from, int to) {
        return this.transactionRepository.findAllTransactionsBetweenRange(from, to);
    }

    public CounterDetailsDTO findNumberOfAllTransactions() {
        return this.transactionRepository.findNumberOfAllTransactions();
    }

    public List<TransactionDetailsDTO> findTransactionsByFromAddress(String from) {
        return this.transactionRepository.findAllTransactionsByFromAddress(from);
    }

    public List<TransactionDetailsDTO> findTransactionsByToAddress(String to) {
        return this.transactionRepository.findAllTransactionsByToAddress(to);
    }
}
