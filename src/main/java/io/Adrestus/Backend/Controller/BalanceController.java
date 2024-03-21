package io.Adrestus.Backend.Controller;

import io.Adrestus.Backend.Service.TransactionWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("api/v1/balance")
@RestController
public class BalanceController {

    @Autowired
    private TransactionWalletService transactionWalletService;

    @RequestMapping(value = "{address}/{zone}", method = RequestMethod.GET)
    public ResponseEntity<String> createAuthenticationToken(@PathVariable("address") String address, @RequestBody @PathVariable("zone") String zone) {
        return new ResponseEntity<String>(transactionWalletService.getAddressBalanceFromZone(address, zone), HttpStatus.OK);
    }
}
