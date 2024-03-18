package io.Adrestus.Backend.Controller;


import io.Adrestus.Backend.DTO.AccountDetailsDTO;
import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.Service.BlockService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.model.AccountModel;
import io.Adrestus.Backend.model.BlockModel;
import io.Adrestus.Backend.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestController
@RequestMapping("/api/v1/explorer")
public class RepositoryController {
    @Autowired
    private AccountStateService accountStateService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/account/{address}")
    public @ResponseBody AccountModel getAccountByAddress(@PathVariable("address") String address) {
        return this.accountService.findAccountByAddress(address);
    }

    @GetMapping("/transactionsByFromAddress/{address}")
    public @ResponseBody List<TransactionDetailsDTO> getTransactionsByFromAddress(@PathVariable("address") String address) {
        return this.transactionService.findTransactionsByFromAddress(address);
    }

    @GetMapping("/transactionsByToAddress/{address}")
    public @ResponseBody List<TransactionDetailsDTO> getTransactionsByToAddress(@PathVariable("address") String address) {
        return this.transactionService.findTransactionsByToAddress(address);
    }

    @GetMapping("/TotalAccountBalanceByAddressZone/{address}")
    public @ResponseBody List<AccountDetailsDTO> findTotalAccountBalanceByAddressZone(@PathVariable("address") String address) {
        return this.accountStateService.findTotalAccountBalanceByAddressZone(address);
    }

    @GetMapping("/transactionsByHash/{hash}")
    public @ResponseBody List<TransactionDetailsDTO> getTransactionsByBlockHash(@PathVariable("hash") String hash) {
        return this.blockService.findAllTransactionsByBlockHash(hash);
    }

    @GetMapping("/transaction/{transaction_hash}")
    public @ResponseBody TransactionModel getTransactionByHash(@PathVariable("transaction_hash") String trhash) {
        return this.transactionService.findByTransactionhash(trhash);
    }

    @GetMapping("/block/{block_hash}")
    public @ResponseBody BlockModel getBlockByHash(@PathVariable("block_hash") String block_hash) {
        return this.blockService.findByBlockhash(block_hash);
    }
}
