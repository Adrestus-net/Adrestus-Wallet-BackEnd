package io.Adrestus.Backend.Service;

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

    public List<AccountModel> findAllActiveAccounts() {
        return this.accountRepository.findAll();
    }

    public AccountModel findByAccountId(String address) {
        return this.accountRepository.findByAccountId(address);
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
}
