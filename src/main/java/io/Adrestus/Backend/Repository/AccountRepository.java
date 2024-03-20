package io.Adrestus.Backend.Repository;

import io.Adrestus.Backend.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    AccountModel findByAddress(String address);

    @Query(value = "SELECT * FROM user.accounts u", nativeQuery = true)
    List<AccountModel> findAllActiveAccounts();

    @Query(value = "SELECT * FROM user.accounts WHERE accounts.Address= ?1", nativeQuery = true)
    AccountModel findAccountByAddress(String address);

    @Query(value = "SELECT * FROM user.accounts WHERE accounts.Address IN :addresses", nativeQuery = true)
    List<AccountModel> findAccountByListOfAddresses(@Param("addresses") List<String> addresses);

    @Query(value = "SELECT * FROM user.accounts ORDER BY accounts.account_id DESC LIMIT 1", nativeQuery = true)
    AccountModel findLastAccountEntry();
}
