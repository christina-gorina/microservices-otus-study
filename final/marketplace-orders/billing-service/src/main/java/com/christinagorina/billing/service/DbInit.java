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
        Account account1 = new Account(1L, "user1", new BigDecimal(200));
        Account account2 = new Account(2L, "user2", new BigDecimal(250));
        Account account3 = new Account(3L, "user3", new BigDecimal(200));

        List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);
        accountList.add(account3);

        accountRepository.saveAll(accountList);
    }
}