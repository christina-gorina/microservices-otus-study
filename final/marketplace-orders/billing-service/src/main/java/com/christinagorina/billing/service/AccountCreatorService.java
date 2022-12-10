package com.christinagorina.billing.service;

import com.christinagorina.billing.model.Account;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountCreatorService {

    public List<Account> createAccount() {
        Account account1 = new Account(1L, "user1", new BigDecimal(200));
        Account account2 = new Account(2L, "user2", new BigDecimal(250));
        Account account3 = new Account(3L, "user3", new BigDecimal(200));

        List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);
        accountList.add(account3);

        return accountList;
    }
}
