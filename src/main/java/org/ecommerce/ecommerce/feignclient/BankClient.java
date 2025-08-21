package org.ecommerce.ecommerce.feignclient;


import org.ecommerce.ecommerce.dtos.FundTransferRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BANK-APPLICATION")
//@FeignClient(name = "bank-service", url = "http://localhost:9091")
public interface BankClient {

    @PostMapping("/api/transactions/transfer")
    String transferFund(@RequestBody FundTransferRequest request);
}


