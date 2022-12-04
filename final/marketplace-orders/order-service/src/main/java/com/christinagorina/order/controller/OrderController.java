package com.christinagorina.order.controller;


import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.model.Order;
import com.christinagorina.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/order")
    public String create(@RequestBody OrderDto orderDto) {
        log.info("order qwe = " + orderDto);
        return orderService.createOrder(orderDto);
    }

    @GetMapping("/api/order/{orderUuid}")
    public Order getOrder(@PathVariable String orderUuid) {
        log.info("order orderUuid qwe = " + orderUuid);
        return orderService.findByOrderUuid(UUID.fromString(orderUuid));
    }

}
