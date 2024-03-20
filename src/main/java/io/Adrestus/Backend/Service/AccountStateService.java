package io.Adrestus.Backend.Service;

import io.Adrestus.Backend.DTO.AccountDetailsDTO;
import io.Adrestus.Backend.Repository.AccountStateRepository;
import io.Adrestus.Backend.model.AccountStateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountStateService {
    @Autowired
    private AccountStateRepository accountStateRepository;


    public void save(AccountStateModel entity) {
        accountStateRepository.save(entity);
    }

    public void saveAll(List<AccountStateModel> entity) {
        accountStateRepository.saveAll(entity);
    }

    public AccountStateModel findByAccountStateObjectAccountAddress(String address) {
        return this.accountStateRepository.findByAccountStateObjectAccountAddress(address);
    }

    public List<AccountStateModel> findAllActiveAccounts() {
        return this.accountStateRepository.findAll();
    }


    public int updateAccountSetBalanceForAddress(double balance, String address, int zoneID) {
        return this.accountStateRepository.updateAccountSetBalanceForAddress(balance, address, zoneID);
    }

    public List<AccountDetailsDTO> findTotalAccountBalanceByAddressZone(String address) {
        return this.accountStateRepository.findTotalAccountBalanceByAddressZone(address);
    }

}
