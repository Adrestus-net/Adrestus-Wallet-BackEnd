package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.Repository.TransactionWalletRepository;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.core.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class TransactionWalletService {
    private final TransactionWalletRepository TransactionRepository;

    public TransactionWalletService(@Qualifier("transactionDao") TransactionWalletRepository TransactionRepository) {
        this.TransactionRepository = TransactionRepository;
    }

    public String addTransaction(Transaction transaction) {
        return this.TransactionRepository.addTransaction(transaction);
    }

    public ResponseDao getTransactionsByAddress(String address) {
        return this.TransactionRepository.getTransactionsByAddress(address);
    }

    public String getAddressBalanceFromZone(String address, String zone) {
        return this.TransactionRepository.getAddressBalanceFromZone(address, zone);
    }
}
