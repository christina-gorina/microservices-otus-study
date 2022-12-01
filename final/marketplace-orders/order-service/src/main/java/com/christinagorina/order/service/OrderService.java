package com.christinagorina.order.service;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.order.mapper.OrderMapper;
import com.christinagorina.order.model.Order;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.status.LockState;
import com.christinagorina.status.OrderStatus;
import com.christinagorina.status.PaymentStatus;
import com.christinagorina.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final Sinks.Many<Message<OrderEvent>> orderSink;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Transactional(rollbackFor = Exception.class) //TODO !!!Рассказать на защите можно
    public String createOrder(OrderDto orderDto) {
        log.info("createOrder qwe start");
        Order order = Optional.ofNullable(orderDto).map(orderMapper::orderDtoToOrder).orElseThrow(IllegalArgumentException::new);
        log.info("orderMapper order qwe = " + order);
        order.setOrderStatus(OrderStatus.NEW);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setDateTime(LocalDateTime.now());
        order.setLockState(LockState.PENDING);
        log.info("order presave qwe = " + order);
        order = orderRepository.save(order);
        log.info("createOrder qwe order = " + order);
        OrderEvent orderEvent = orderMapper.orderToOrderEvent(order, orderDto);

        Message<OrderEvent> orderEventMsg = MessageBuilder
                .withPayload(orderEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, order.getOrderUuid())
                .build();
        log.info("orderEventMsg qwe = " + orderEventMsg);

        //TODO идемпотентность входящих в BFF сообщений

        //TODO идемпотентность изменяемых и изменяющих транзакций, т к брокер может слать сообщение несколько раз?, и как коммитеть в брокер после каждого сообщения мб?

        //TODO при бронировании может не хватить изоляции, смотреть как выставить + пример просто с кафка, там тоже есть уровни изоляции
//TODO при успешной отправке в брокер делать идемпотентность
        orderSink.tryEmitNext(orderEventMsg);
        return "New order with uuid " + orderDto.getOrderUuid() + " created";

    }

}
