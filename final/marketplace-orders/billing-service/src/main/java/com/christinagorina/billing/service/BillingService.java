package com.christinagorina.billing.service;

import com.christinagorina.billing.repository.AccountRepository;
import com.christinagorina.events.logistics.LogisticsEvent;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.payment.BillingEvent;
import com.christinagorina.status.BillingStatus;
import com.christinagorina.status.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final AccountRepository accountRepository;

    /*@Transactional(isolation = Isolation.REPEATABLE_READ) //TODO точно не SERIALIZABLE?
    public BillingEvent paymentReservation(OrderEvent orderEvent) {
        return accountRepository.findById(orderEvent.getUserId())
                .filter(account -> account.getBalance().compareTo(orderEvent.getPrice()) >= 0)
                .map(account -> {
                    account.setBalance(account.getBalance().subtract(orderEvent.getPrice()));
                    account.setOrderStatus(OrderStatus.RESERVED);
                    return BillingEvent.builder()
                            .status(BillingStatus.APPROVED)
                            .build();
                })
                .orElse(BillingEvent.builder()
                        .status(BillingStatus.REJECTED)
                        .build());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ) //TODO точно не SERIALIZABLE?
    public void cancelPaymentReservation(OrderEvent orderEvent) {

    }*/



}
