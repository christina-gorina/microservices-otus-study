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
                .orderUuid(orderDto.getOrderUuid())
                .userId(orderDto.getUserId())
                .point(orderDto.getPoint())
                .pointId(orderDto.getPointId())
                .addressX(orderDto.getAddressX())
                .addressY(orderDto.getAddressY())
                .price(orderDto.getPrice())
                .build();
    }

    public OrderEvent orderToOrderEvent(@NonNull Order order, @NonNull OrderDto orderDto){
        log.info("orderToOrderEvent qwe start");
        return OrderEvent.builder()
                .orderId(order.getId())
                .productItemsUuidAndCount(orderDto.getProductItemsUuidAndCount())
                .orderStatus(order.getOrderStatus())
                .addressX(order.getAddressX())
                .addressY(order.getAddressY())
                .userId(order.getUserId())
                .price(order.getPrice())
                .build();

    }
}
