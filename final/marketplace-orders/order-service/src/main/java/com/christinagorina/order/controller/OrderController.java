package com.christinagorina.order.controller;


import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/order")
    public String create(@RequestBody OrderDto orderDto) throws IOException {
        log.info("order qwe = " + orderDto);
        return orderService.createOrder(orderDto);
    }

}
