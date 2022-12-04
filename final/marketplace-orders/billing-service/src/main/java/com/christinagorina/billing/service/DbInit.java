package com.christinagorina.billing.service;

import com.christinagorina.billing.model.Account;
import com.christinagorina.billing.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbInit {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    private void postConstruct() {
        Account account1 = new Account(1L, 1L, new BigDecimal(200));
        Account account2 = new Account(2L, 2L, new BigDecimal(250));
        Account account3 = new Account(3L, 3L, new BigDecimal(150));
        Account account4 = new Account(4L, 4L, new BigDecimal(220));
        Account account5 = new Account(5L, 5L, new BigDecimal(180));

        List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);
        accountList.add(account3);
        accountList.add(account4);
        accountList.add(account5);

        accountRepository.saveAll(accountList);
    }
}