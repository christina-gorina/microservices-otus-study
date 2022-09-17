package com.christinagorina.controller;

import com.christinagorina.model.*;
import com.christinagorina.repository.IdempotencyStorageRepository;
import com.christinagorina.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;
    private final IdempotencyStorageRepository idempotencyStorageRepository;

    @PostMapping("/api/v1/order")
    public String create(@RequestBody OrderTo orderTo) {
        //3d576ddb-68e6-4863-8d96-3085d13d6bec  !idempotencyKey
        //f7e96069-5381-4bf7-9607-2b7a0d72e301 or f7e96069-5381-4bf7-9607-2b7a0d72e302 !orderIdentifier
        Optional.of(orderTo).filter(o -> Objects.nonNull(o.getIdempotencyKey())).orElseThrow(IllegalArgumentException::new);
        IdempotencyStorage idempotencyStorage = idempotencyStorageRepository.findByIdempotencyKey(orderTo.getIdempotencyKey());
        String answer;
        if (Objects.isNull(idempotencyStorage)) {
            Order order = Order.builder()
                    .orderIdentifier(orderTo.getOrderIdentifier())
                    .state(orderTo.getState())
                    .buyerId(orderTo.getBuyerId())
                    .product(orderTo.getProduct())
                    .creationDateTime(LocalDateTime.now())
                    .build();
            order = Optional.of(orderRepository.save(order)).orElseThrow(IllegalArgumentException::new);
            idempotencyStorage = IdempotencyStorage.builder()
                    .idempotencyKey(orderTo.getIdempotencyKey())
                    .orderId(order.getId())
                    .creationDateTime(order.getCreationDateTime())
                    .build();
            Optional.of(idempotencyStorageRepository.save(idempotencyStorage)).filter(i -> Objects.nonNull(i.getId())).orElseThrow(IllegalArgumentException::new);
            answer = "Order " + order.getOrderIdentifier() + " now created";
        } else {
            Order order = Optional.of(orderRepository.getById(idempotencyStorage.getOrderId())).orElseThrow(IllegalArgumentException::new);
            answer = "Order " + order.getOrderIdentifier() + " already created and has state " + order.getState();
        }

        return answer;
    }

    @GetMapping("/api/v1/order/orderIdentifier/{orderIdentifier}")
    public Order findByOrderIdentifier(@PathVariable("orderIdentifier") String orderIdentifier) {
        return Optional.of(orderRepository.findByOrderIdentifier(orderIdentifier)).orElse(null);
    }

    @PutMapping("/api/v1/order/orderIdentifier/{orderIdentifier}/state/{state}")
    public Order stateUpdate(@PathVariable String orderIdentifier, @PathVariable State state) {
        Order existingOrder = Optional.of(orderRepository.findByOrderIdentifier(orderIdentifier)).orElseThrow(IllegalArgumentException::new);
        existingOrder.setState(state);
        return orderRepository.save(existingOrder);
    }

    @DeleteMapping("/api/v1/order/clean")
    public String cleanOrders() {
        orderRepository.deleteAll();
        return "Order storage cleaned";
    }

    @DeleteMapping("/api/v1/idempotencyStorage/clean")
    public String cleanIdempotencyStorage() {
        idempotencyStorageRepository.deleteAll();
        return "Idempotency storage cleaned";
    }

    @GetMapping("/api/v1/delay")
    public ResponseEntity<Answer> delay() throws InterruptedException {
        Thread.sleep(20000); //20sec
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Answer("OK"));
    }

    @GetMapping("/api/v1/health")
    public ResponseEntity<Answer> health() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Answer("OK"));
    }

}


