package com.christinagorina.order.config;

import com.christinagorina.events.payment.BillingEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@Slf4j
@AllArgsConstructor
public class ResultConsumer {

    @Bean
    public Consumer<Message<BillingEvent>> orderCheckResultConsumer() {
        log.info("orderCheckResultConsumer qwe");
        return billingEvent -> {
            log.info("billingConsumer qwe = " + billingEvent.getPayload());
        };
    }

}