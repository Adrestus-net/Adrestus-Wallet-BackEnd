package io.Adrestus.Backend.Controller;

import io.Adrestus.Backend.Service.TransactionWalletService;
import io.Adrestus.bloom_filter.core.BloomObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RequestMapping("api/v1/balance")
@RestController
public class BalanceController {

    @Autowired
    private TransactionWalletService transactionWalletService;

    @RequestMapping(value = "{zone}", method = RequestMethod.POST)
    public @ResponseBody HashMap<String, String> createAuthenticationToken(@RequestBody BloomObject bloomObject, @RequestBody @PathVariable("zone") String zone) {
        return transactionWalletService.getTransactionsBalance(bloomObject, zone);
    }
}
