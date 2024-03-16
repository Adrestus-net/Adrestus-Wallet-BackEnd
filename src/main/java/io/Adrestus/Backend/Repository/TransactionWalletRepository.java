package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.core.Transaction;

import java.util.HashMap;

public interface TransactionWalletRepository {

    public String addTransaction(Transaction transaction);

    public int updateTransactionByAddress(String hash, Transaction transaction);

    public ResponseDao getTransactionsByAddress(String address);

    public  HashMap<String, String> getTransactionsBalance(BloomObject bloomObject,String zone);

    public HashMap<String, ResponseDao> getTransactionsByBloomFilter(BloomObject bloomObject);

    public int deleteALL();
}
