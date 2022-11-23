package com.christinagorina.order.service;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.mapper.OrderMapper;
import com.christinagorina.order.model.Order;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.status.OrderStatus;
import com.christinagorina.status.PaymentStatus;
import com.christinagorina.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    //TODO убрать везде @Autowired
    @Autowired
    private Sinks.Many<OrderEvent> orderSink;
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
        log.info("order presave qwe = " + order);
        order = orderRepository.save(order);
        log.info("createOrder qwe order = " + order);
        OrderEvent orderEvent = orderMapper.orderToOrderEvent(order, orderDto);

        //TODO проработать ситуацию, когда брокер на стороне получателя получил сообщение, отправил в брокер что он его получил,
        // а потом упал не успев обработать. Возможно начинать заново обработку по статусу в бд при старте, см ссылки в избранном

        //TODO идемпотентность входящих в BFF сообщений

        //TODO идемпотентность изменяемых и изменяющих транзакций, т к брокер может слать сообщение несколько раз?, и как коммитеть в брокер после каждого сообщения мб?

        //TODO при бронировании может не хватить изоляции, смотреть как выставить + пример просто с кафка, там тоже есть уровни изоляции

        //TODO this убрать
        this.orderSink.tryEmitNext(Optional.ofNullable(orderEvent).orElseThrow(IllegalArgumentException::new));
        // TODO что такое orThrow или orThrowWithCause может можно ошибку кидать в случае чего?
        // this.orderSink.tryEmitNext(Optional.ofNullable(orderEvent).orElseThrow(IllegalArgumentException::new)).orThrow();
        return "New order with uuid " + orderDto.getUuid() + " created";

    }

}
