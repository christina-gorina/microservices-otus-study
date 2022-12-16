package com.christinagorina.order.config;

import com.christinagorina.events.OrderEvent;
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
public class OrderConfig {

    @Bean
    public Sinks.Many<Message<OrderEvent>> orderSink(){
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<OrderEvent>>> orderSupplier(Sinks.Many<Message<OrderEvent>> sink){
        return sink::asFlux;
    }

}
