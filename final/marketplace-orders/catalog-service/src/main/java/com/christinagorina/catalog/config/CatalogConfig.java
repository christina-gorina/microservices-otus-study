package com.christinagorina.catalog.config;

import com.christinagorina.catalog.service.CatalogService;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

@Configuration
@Slf4j
@AllArgsConstructor
public class CatalogConfig {

    private final CatalogService catalogService;

    //TODO Почему так 1_2?
    @Bean
    public Function<Flux<OrderEvent>, Flux<CatalogEvent>> catalogProcessor() {
        log.info("catalogProcessor qwe");
        return flux -> flux.flatMap(this::process);
    }

    //TODO Почему так 2_2?
    private Mono<CatalogEvent> process(@NotNull OrderEvent orderEvent){
        log.info("catalog2 event take qwe = " + orderEvent);
        if(OrderStatus.NEW.equals(orderEvent.getOrderStatus())){
            log.info("order event is new");
            return Mono.fromSupplier(() -> catalogService.productItemReservation(orderEvent));
        }
        return Mono.fromRunnable(() -> catalogService.cancelProductItemReservation(orderEvent));
    }

}

