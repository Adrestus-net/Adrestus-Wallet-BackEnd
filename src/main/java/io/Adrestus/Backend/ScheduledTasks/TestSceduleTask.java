package io.Adrestus.Backend.ScheduledTasks;


import io.Adrestus.Backend.Config.APIConfiguration;
import io.Adrestus.Backend.Repository.AccountRepository;
import io.Adrestus.Backend.Repository.BlockRepository;
import io.Adrestus.Backend.Repository.TransactionRepository;
import io.Adrestus.Backend.Repository.UserRepository;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.BlockService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.model.AccountModel;
import io.Adrestus.Backend.model.BlockModel;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TestSceduleTask {
    private int counter;

    public TestSceduleTask() {
        counter=0;
    }

    @Autowired
    @Lazy
    private AccountService accountService;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    @Autowired
    @Lazy
    private BlockService blockService;

    @Scheduled(fixedRate = APIConfiguration.TRANSACTION_BLOCK_RATE)
    public void test() throws InterruptedException {

        AccountModel accountModel=new AccountModel();
        accountModel.setBalance(100);
        accountModel.setAddress("from"+counter);
        accountModel.setStaked(50);
        BlockModel blockModel=new BlockModel();
        blockModel.setBlockhash("blockhash"+counter);
        blockModel.setHeight(1);
        blockModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        TransactionModel transactionModel=new TransactionModel();
        TransactionModel transactionModel1=new TransactionModel();
        transactionModel.setTo("to1");
        transactionModel.setTransactionhash("trhash"+counter);
        transactionModel.setBlockModel(blockModel);
        transactionModel.setFrom(accountModel.getAddress());

        transactionModel1.setTo("to1");
        transactionModel1.setTransactionhash("trhash"+counter+1);
        transactionModel1.setBlockModel(blockModel);
        transactionModel1.setFrom(accountModel.getAddress());

        accountService.save(accountModel);
        blockService.save(blockModel);
        transactionService.save(transactionModel);
        transactionService.save(transactionModel1);

        int g=accountService.updateAccountSetBalanceForAddress(200,"from0");
        accountModel.setStaked(300);
        accountService.save(accountModel);
        counter++;
    }
}
