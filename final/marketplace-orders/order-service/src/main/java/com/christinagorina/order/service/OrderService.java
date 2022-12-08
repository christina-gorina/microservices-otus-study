package com.christinagorina.order.service;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.events.BillingEvent;
import com.christinagorina.order.mapper.OrderMapper;
import com.christinagorina.order.model.Order;
import com.christinagorina.events.OrderEvent;
import com.christinagorina.status.OrderStatus;
import com.christinagorina.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final Sinks.Many<Message<OrderEvent>> orderSink;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Transactional(rollbackFor = Exception.class)
    public String createOrder(OrderDto orderDto) {
        Order order = Optional.ofNullable(orderDto).map(orderMapper::orderDtoToOrder).orElseThrow(IllegalArgumentException::new);
        order.setOrderStatus(OrderStatus.NEW);
        order.setDateTime(LocalDateTime.now());
        order = orderRepository.save(order);
        log.info("createOrder order = " + order);
        OrderEvent orderEvent = orderMapper.orderToOrderEvent(order, orderDto);

        Message<OrderEvent> orderEventMsg = MessageBuilder
                .withPayload(orderEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, order.getId())
                .build();
        log.info("orderEventMsg = " + orderEventMsg);

        orderSink.tryEmitNext(orderEventMsg);
        return "New order with uuid " + orderDto.getOrderUuid() + " created";

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void orderResult(BillingEvent billingEvent) {
        Order order = orderRepository.findById(billingEvent.getOrderId()).orElseThrow();
        order.setOrderStatus(billingEvent.getOrderStatus());
        order.setUserMessage(billingEvent.getUserMessage());
        order = orderRepository.save(order);
        log.info("order result = " + order);
    }

    public Order findByOrderUuid(UUID orderUuid) {
        return orderRepository.findByOrderUuid(orderUuid).orElseThrow();
    }

}
