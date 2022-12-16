package com.christinagorina.billing.config;

import com.christinagorina.events.BillingEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Configuration
public class ResultConfig {

    @Bean
    public Sinks.Many<Message<BillingEvent>> resultSink(){
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<BillingEvent>>> resultSupplier(Sinks.Many<Message<BillingEvent>> sink){
        return sink::asFlux;
    }

}
