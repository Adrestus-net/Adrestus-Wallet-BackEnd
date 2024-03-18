package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.DTO.AccountDetailsDTO;
import io.Adrestus.Backend.model.AccountStateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountStateRepository extends JpaRepository<AccountStateModel, Long> {

    @Query(value = "SELECT * FROM user.account_state WHERE account_state.account_id= ?", nativeQuery = true)
    AccountStateModel findByAccountStateObjectAccountId(Long accountId);

    @Query(value = "SELECT * FROM user.account_state u", nativeQuery = true)
    List<AccountStateModel> findAllActiveAccounts();

    @Modifying
    @Transactional
    @Query(value = "UPDATE user.account_state INNER JOIN user.accounts ON accounts.account_id=account_state.account_id SET balance = ? WHERE accounts.address = ? AND zoneid=?;", nativeQuery = true)
    int updateAccountSetBalanceForAddress(double balance, String address, int zoneId);


    @Query(value =
            "SELECT account_state.account_id as accountId, accounts.address as address,SUM(balance) as balance, SUM(staked) as staked\n" +
                    "FROM user.account_state\n" +
                    "INNER JOIN user.accounts ON accounts.account_id=account_state.account_id\n" +
                    "WHERE accounts.address = 'from0'\n" +
                    "GROUP BY account_state.account_id;", nativeQuery = true)
    List<AccountDetailsDTO> findTotalAccountBalanceByAddressZone(String address);
}
