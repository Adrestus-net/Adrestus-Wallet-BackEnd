package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.DTO.CounterDetailsDTO;
import io.Adrestus.Backend.DTO.LimitTransactionsDetailsDTO;
import io.Adrestus.Backend.Repository.AccountRepository;
import io.Adrestus.Backend.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public void save(AccountModel entity) {
        accountRepository.save(entity);
    }

    public void saveAll(List<AccountModel> entity) {
        accountRepository.saveAll(entity);
    }

    public List<AccountModel> findAllActiveAccounts() {
        return this.accountRepository.findAll();
    }

    public AccountModel findByAddress(String address) {
        return this.accountRepository.findByAddress(address);
    }

    public AccountModel findAccountByAddress(String address) {
        return this.accountRepository.findAccountByAddress(address);
    }

    public List<AccountModel> findAccountByListOfAddresses(@Param("addresses") List<String> addresses) {
        return this.accountRepository.findAccountByListOfAddresses(addresses);
    }

    public AccountModel findLastAccountEntry() {
        return this.accountRepository.findLastAccountEntry();
    }


    public CounterDetailsDTO findNumberOfTransactionsByAccountAddress(String address) {
        return this.accountRepository.findNumberOfTransactionsByAccountAddress(address);
    }

    public List<LimitTransactionsDetailsDTO> findTransactionByAccountAddressInRange(String address, int from, int to) {
        return this.accountRepository.findTransactionByAccountAddressInRange(address, from, to);
    }
}
