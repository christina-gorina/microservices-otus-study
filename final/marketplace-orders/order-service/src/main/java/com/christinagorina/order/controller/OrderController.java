package com.christinagorina.order.controller;


import com.christinagorina.order.model.Order;
import com.christinagorina.order.service.OrderStatusPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderStatusPublisher publisher;

    @PostMapping("/api/order")
    public String  create(@RequestBody Order order) {
        log.info("order = " + order);
        this.publisher.raiseOrderEvent();

        return "succes";
    }

}
