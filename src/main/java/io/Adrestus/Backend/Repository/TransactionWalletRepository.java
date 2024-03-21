package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.core.Transaction;

public interface TransactionWalletRepository {

    public String addTransaction(Transaction transaction);

    public ResponseDao getTransactionsByAddress(String address);

    public String getAddressBalanceFromZone(String address, String zone);

}
