package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Repository.AccountRepository;
import io.Adrestus.Backend.model.AccountModel;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    public void save(AccountModel entity){
        accountRepository.save(entity);
    }


    public AccountModel findByAddress(String address) {
        return this.accountRepository.findByAddress(address);
    }

    public List<TransactionDetailsDTO> findTransactionsByFromAddress(String from) {
        return this.accountRepository.findAllTransactionsByFromAddress(from);
    }
    public List<TransactionDetailsDTO> findTransactionsByToAddress(String to){
        return this.accountRepository.findAllTransactionsByToAddress(to);
    }

    public int updateAccountSetBalanceForAddress(double balance, String address){
        return this.accountRepository.updateAccountSetBalanceForAddress(balance,address);
    }

}
