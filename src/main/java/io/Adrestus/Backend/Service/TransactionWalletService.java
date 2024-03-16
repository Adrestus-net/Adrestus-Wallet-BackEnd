package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.Repository.TransactionWalletRepository;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.core.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class TransactionWalletService {
    private final TransactionWalletRepository TransactionRepository;

    public TransactionWalletService(@Qualifier("transactionDao") TransactionWalletRepository TransactionRepository) {
        this.TransactionRepository = TransactionRepository;
    }

    public String addTransaction(Transaction transaction) {
        return this.TransactionRepository.addTransaction(transaction);
    }

    public int updateTransactionByAddress(String from, Transaction transaction) {
        return this.TransactionRepository.updateTransactionByAddress(from, transaction);
    }

    public ResponseDao getTransactionsByAddress(String address) {
        return this.TransactionRepository.getTransactionsByAddress(address);
    }

    public HashMap<String, ResponseDao> getTransactionsByBloomFilter(BloomObject bloomObject) {
        return this.TransactionRepository.getTransactionsByBloomFilter(bloomObject);
    }

    public  HashMap<String, String>getTransactionsBalance(BloomObject bloomObject,String zone) {
        return this.TransactionRepository.getTransactionsBalance(bloomObject,zone);
    }
    public int deleteALL() {
        return this.TransactionRepository.deleteALL();
    }
}
