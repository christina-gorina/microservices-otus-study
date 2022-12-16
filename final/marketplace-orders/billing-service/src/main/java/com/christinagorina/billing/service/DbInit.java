package com.christinagorina.billing.service;

import com.christinagorina.billing.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbInit {

    private final AccountRepository accountRepository;
    private final AccountCreatorService accountCreatorService;

    @PostConstruct
    private void postConstruct() {
        log.info("postConstruct createProductItem");
        accountRepository.saveAll(accountCreatorService.createAccount());
    }
}