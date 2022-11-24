package com.christinagorina.order.mapper;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.model.Order;
import com.christinagorina.events.order.OrderEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderMapper {

    public Order orderDtoToOrder(@NonNull OrderDto orderDto){
        log.info("orderDtoToOrder qwe start");
        return Order.builder()
                .uuid(orderDto.getUuid())
                .userId(orderDto.getUserId())
                .point(orderDto.getPoint())
                .pointId(orderDto.getPointId())
                .address(orderDto.getAddress())
                .price(orderDto.getPrice())
                .build();
    }

    public OrderEvent orderToOrderEvent(@NonNull Order order, @NonNull OrderDto orderDto){
        log.info("orderToOrderEvent qwe start");
        return OrderEvent.builder()
                .uuid(order.getUuid())
                .productItemsIdAndCount(orderDto.getProductItemsIdAndCount())
                .orderStatus(order.getOrderStatus())
                .userId(order.getUserId())
                .price(order.getPrice())
                .build();

    }
}