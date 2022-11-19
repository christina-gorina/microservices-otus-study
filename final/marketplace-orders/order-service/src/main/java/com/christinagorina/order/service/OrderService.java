package com.christinagorina.order.service;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.order.mapper.OrderMapper;
import com.christinagorina.order.model.Order;
import com.christinagorina.order.model.OrderEvent2;
import com.christinagorina.order.model.OrderStatus;
import com.christinagorina.order.model.PaymentStatus;
import com.christinagorina.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private Sinks.Many<OrderEvent> orderSink;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public String createOrder(OrderDto orderDto){
        log.info("createOrder qwe start");
        Order order = Optional.ofNullable(orderDto).map(orderMapper::orderDtoToOrder).orElseThrow(IllegalArgumentException::new);
        log.info("orderMapper order qwe = " + order);
        order.setState(OrderStatus.NEW);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setDateTime(LocalDateTime.now());
        log.info("order presave qwe = " + order);
        order = orderRepository.save(order);
        log.info("createOrder qwe order = " + order);
        OrderEvent2 orderEvent2 = Optional.of(order).map(orderMapper::orderToOrderEvent).orElseThrow(IllegalArgumentException::new);

        //TODO Остановилась здесь, отправлять не OrderEvent, а OrderEvent2 и ловить его на другой стороне
//////////////////////////////////////////////////////////////
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setLastName("LastName");
        orderEvent.setName("Name");
        this.orderSink.tryEmitNext(orderEvent);
        return "New order with uuid " + orderDto.getUuid() + " created";

    }

}
