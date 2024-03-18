package io.Adrestus.Backend.Controller;

import io.Adrestus.Backend.Service.TransactionWalletService;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@CrossOrigin
@RequestMapping("api/v1/transaction")
@RestController
public class TransactionController {
    @Autowired
    private final TransactionWalletService transactionWalletService;

    @Autowired
    public TransactionController(TransactionWalletService transactionWalletService) {
        this.transactionWalletService = transactionWalletService;
    }


    @PostMapping
    public @ResponseBody String addTransaction(@RequestBody Transaction transaction) {
        return this.transactionWalletService.addTransaction(transaction);
    }

    @GetMapping(path = {"{from}"})
    public @ResponseBody ResponseDao getTransactionsByAddress(@RequestBody @PathVariable("from") String address) {
        return this.transactionWalletService.getTransactionsByAddress(address);
    }

    @PostMapping(value = "/bloom_filter")
    public @ResponseBody HashMap<String, ResponseDao> getTransactionsByBloomFilter(@RequestBody BloomObject bloomObject) {
        return this.transactionWalletService.getTransactionsByBloomFilter(bloomObject);
    }

}
