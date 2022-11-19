package com.christinagorina.order.controller;


import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

}
