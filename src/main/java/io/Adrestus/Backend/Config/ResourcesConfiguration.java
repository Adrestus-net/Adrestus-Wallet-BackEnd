package io.Adrestus.Backend.Config;

import io.Adrestus.Backend.MemoryBuffer.AddressMemoryInstance;
import io.Adrestus.Backend.Repository.AccountRepository;
import io.Adrestus.Backend.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.TreeSet;

@Configuration
@EnableJpaRepositories("io.Adrestus.Backend.Repository")
public class ResourcesConfiguration {

    @Autowired
    private AccountRepository accountRepository;


    @Bean
    public int StartCachingAddress() {
        List<AccountModel> accounts = accountRepository.findAllActiveAccounts();
        TreeSet<String> treeSet = new TreeSet<>();
        accounts.stream().forEach(val -> treeSet.add(val.getAddress()));
        treeSet.add("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP");
        AddressMemoryInstance.getInstance().setMemory(treeSet);
        return 0;
    }
}
