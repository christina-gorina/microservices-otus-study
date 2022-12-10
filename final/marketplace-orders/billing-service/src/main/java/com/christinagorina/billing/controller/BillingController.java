package com.christinagorina.billing.controller;

import com.christinagorina.billing.model.Account;
import com.christinagorina.billing.repository.AccountRepository;
import com.christinagorina.billing.service.AccountCreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BillingController {

    private final AccountRepository accountRepository;
    private final AccountCreatorService accountCreatorService;

    @PostMapping("/api/billingDbReInit")
    public List<Account> billingDbReInit() {
        log.info("billingDbReInit");
        accountRepository.deleteAll();
        List<Account> accountList = accountRepository.saveAll(accountCreatorService.createAccount());
        log.info("accountList = " + accountList);
        return accountList;
    }

}
