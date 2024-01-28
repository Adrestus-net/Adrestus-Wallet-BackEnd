package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.Repository.KVRepository;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.core.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class TransactionService {
    private final KVRepository KVRepository;

    public TransactionService(@Qualifier("transactionDao") KVRepository KVRepository) {
        this.KVRepository = KVRepository;
    }

    public String addTransaction(Transaction transaction) {
        return this.KVRepository.addTransaction(transaction);
    }

    public int updateTransactionByAddress(String from, Transaction transaction) {
        return this.KVRepository.updateTransactionByAddress(from, transaction);
    }

    public ResponseDao getTransactionsByAddress(String address) {
        return this.KVRepository.getTransactionsByAddress(address);
    }

    public HashMap<String, ResponseDao> getTransactionsByBloomFilter(BloomObject bloomObject) {
        return this.KVRepository.getTransactionsByBloomFilter(bloomObject);
    }

    public  HashMap<String, String>getTransactionsBalance(BloomObject bloomObject,String zone) {
        return this.KVRepository.getTransactionsBalance(bloomObject,zone);
    }
    public int deleteALL() {
        return this.KVRepository.deleteALL();
    }
}
