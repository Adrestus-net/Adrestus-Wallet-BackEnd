package io.Adrestus.Backend.Controller;

import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.bloom_filter.core.BloomObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RequestMapping("api/v1/balance")
@RestController
public class BalanceController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "{zone}", method = RequestMethod.POST)
    public @ResponseBody HashMap<String, String> createAuthenticationToken(@RequestBody BloomObject bloomObject,@RequestBody @PathVariable("zone") String zone) {
        return transactionService.getTransactionsBalance(bloomObject,zone);
    }
}
