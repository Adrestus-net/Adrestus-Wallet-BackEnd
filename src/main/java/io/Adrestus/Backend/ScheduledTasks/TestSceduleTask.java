package io.Adrestus.Backend.ScheduledTasks;


import io.Adrestus.Backend.Config.APIConfiguration;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.Service.BlockService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.implementation.SocketController;
import io.Adrestus.Backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TestSceduleTask {
    private int counter;

    private SocketController socketController;

    public TestSceduleTask() {
        this.socketController = new SocketController();
        counter = 0;
    }

    @Autowired
    @Lazy
    private AccountStateService accountStateService;

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

//        ArrayList<String>ar=new ArrayList<>();
//        ar.add("from1");
//        ar.add("from2");
//        List<AccountModel> fd=accountService.findAccountByListOfAddresses(ar);
        AccountModel ret = accountService.findLastAccountEntry();
        Long count = Long.valueOf(0);
        if (ret != null)
            count = ret.getAccountId() + 1;
        AccountStateModel accountStateModel = new AccountStateModel();
        accountStateModel.setBalance(100);
        accountStateModel.setStaked(50);
        AccountModel accountModel = new AccountModel();
        accountModel.setAccountId(count);
        accountModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        accountModel.setAddress("from" + counter);
        this.socketController.send(accountModel.getAddress());
        BlockModel blockModel = new BlockModel();
        blockModel.setBlockhash("blockhash" + counter);
        blockModel.setHeight(1);
        blockModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        TransactionModel transactionModel = new TransactionModel();
        TransactionModel transactionModel1 = new TransactionModel();
        transactionModel.setTo("to1");
        transactionModel.setTransactionhash("trhash" + counter);
        transactionModel.setBlockModel(blockModel);
        transactionModel.setFrom(accountModel.getAddress());

        transactionModel1.setTo("to1");
        transactionModel1.setTransactionhash("trhash" + counter + 1);
        transactionModel1.setBlockModel(blockModel);
        transactionModel1.setFrom(accountModel.getAddress());

        AccountModel md = accountService.findAccountByAddress(accountModel.getAddress());
        if (md == null)
            accountService.save(accountModel);
        AccountStateObject accountStateObject = new AccountStateObject();
        accountStateObject.setAccountId(count - 1);
        accountStateObject.setZoneId(0);
        accountStateModel.setAccountStateObject(accountStateObject);
        AccountStateModel md2 = accountStateService.findByAccountStateObjectAccountId(count - 1);
        if (md2 == null) {
            try {
                accountStateService.save(accountStateModel);
            } catch (Exception e) {
                int g = 3;
            }
        }
        blockService.save(blockModel);
        transactionService.save(transactionModel);
        transactionService.save(transactionModel1);

        int g = accountStateService.updateAccountSetBalanceForAddress(500, "from1", 0);
//        accountStateModel.setStaked(300);
//        accountStateService.save(accountStateModel);
        counter++;
    }
}
