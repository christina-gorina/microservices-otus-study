package com.christinagorina.billing.service;

import com.christinagorina.billing.repository.AccountRepository;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.payment.BillingEvent;
import com.christinagorina.status.BillingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final AccountRepository accountRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BillingEvent paymentReservation(OrderEvent orderEvent) {
        return accountRepository.findById(orderEvent.getUserId())
                .filter(account -> account.getBalance().compareTo(orderEvent.getPrice()) >= 0)
                .map(account -> {
                    account.setBalance(account.getBalance().subtract(orderEvent.getPrice()));
                    return BillingEvent.builder()
                            .status(BillingStatus.APPROVED)
                            .build();
                })
                .orElse(BillingEvent.builder()
                        .status(BillingStatus.REJECTED)
                        .build());
        //TODO все таки сделать поле резерв и списывать деньги в повторяемой транзакци
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void cancelPaymentReservation(OrderEvent orderEvent) {

    }

}
