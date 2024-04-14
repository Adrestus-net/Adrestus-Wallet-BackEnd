package io.Adrestus.Backend.implementation;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Repository.TransactionWalletRepository;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.Service.BlockService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.Util.ConverterUtil;
import io.Adrestus.Backend.model.BlockModel;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.MemoryTreePool;
import io.Adrestus.TreeFactory;
import io.Adrestus.Trie.PatriciaTreeNode;
import io.Adrestus.config.APIConfiguration;
import io.Adrestus.core.Transaction;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository("transactionDao")
public class TransactionWalletImplementation implements TransactionWalletRepository {


    @Autowired
    private AccountStateService accountStateService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BlockService blockService;


    @Override
    public String addTransaction(Transaction transaction) {
        BlockModel blockModel = blockService.findByBlockhash("0359b4704f3026e59d8837fde6f2ed075fc3642a0dcc79cc0dd23abfa7fcf851");
        transactionService.save(ConverterUtil.convert(transaction, blockModel,1));
//        MessageListener messageListener = new MessageListener();
//        Strategy transactionStrategy = new Strategy(new TransactionStrategy(transaction, messageListener));
//        transactionStrategy.SendTransactionSync();
//        if (messageListener.getConsume_list().stream().anyMatch(val -> val.equals(APIConfiguration.MSG_FAILED)))
//            return APIConfiguration.MSG_FAILED;
        return APIConfiguration.MSG_SUCCESS;
    }

    @Override
    public ResponseDao getTransactionsByAddress(String address) {
        ArrayList<Transaction> from = new ArrayList<>();
        ArrayList<Transaction> to = new ArrayList<>();
        List<TransactionDetailsDTO> fromModel = transactionService.findTransactionsByFromAddress(address);
        List<TransactionDetailsDTO> toModel = transactionService.findTransactionsByToAddress(address);
        fromModel.stream().forEach(val -> from.add(ConverterUtil.convert(val)));
        toModel.stream().forEach(val -> to.add(ConverterUtil.convert(val)));
        return new ResponseDao(from, to);
    }

    @Override
    public String getAddressBalanceFromZone(String address, String zone) {
        MemoryTreePool memoryTreePool = (MemoryTreePool) TreeFactory.getMemoryTree(Integer.parseInt(zone));
        Option<PatriciaTreeNode> res = memoryTreePool.getByaddress(address);
        if (res.isEmpty()) {
            return "0";
        } else
            return String.valueOf(res.get().getAmount());
    }

}
