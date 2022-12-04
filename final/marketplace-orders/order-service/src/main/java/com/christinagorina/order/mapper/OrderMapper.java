package com.christinagorina.order.mapper;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.model.Order;
import com.christinagorina.events.OrderEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderMapper {

    public Order orderDtoToOrder(@NonNull OrderDto orderDto){
        return Order.builder()
                .userId(orderDto.getUserId())
                .addressX(orderDto.getAddressX())
                .addressY(orderDto.getAddressY())
                .price(orderDto.getPrice())
                .orderUuid(orderDto.getOrderUuid())
                .build();
    }

    public OrderEvent orderToOrderEvent(@NonNull Order order, @NonNull OrderDto orderDto){
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
