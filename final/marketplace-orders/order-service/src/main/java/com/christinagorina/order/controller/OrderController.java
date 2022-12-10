package com.christinagorina.order.controller;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.dto.AnswerDto;
import com.christinagorina.order.model.Order;
import com.christinagorina.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/order")
    public String create(@RequestBody OrderDto orderDto, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        log.info("orderDto = " + orderDto);
        log.info("userName = " + orderDto.getUserName());
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(orderDto.getUserName())).orElseThrow(IllegalAccessException::new);
        return orderService.createOrder(orderDto);
    }

    @GetMapping("/api/order/{orderUuid}/userName/{userName}")
    public Order getOrder(@PathVariable String orderUuid, @PathVariable String userName, @RequestHeader("x-auth-user") String xAuthUser) throws IllegalAccessException {
        log.info("orderUuid  = " + orderUuid);
        log.info("userName = " + userName);
        Optional.ofNullable(xAuthUser).filter(n -> n.equals(userName)).orElseThrow(IllegalAccessException::new);
        return orderService.findByOrderUuid(UUID.fromString(orderUuid));
    }

    @GetMapping("/api/order/delay")
    public ResponseEntity<AnswerDto> delay() throws InterruptedException {
        Thread.sleep(10000); //10sec
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AnswerDto("OK"));
    }

}
