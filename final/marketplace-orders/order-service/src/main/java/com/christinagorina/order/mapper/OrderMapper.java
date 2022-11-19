package com.christinagorina.order.mapper;

import com.christinagorina.dto.OrderDto;
import com.christinagorina.order.model.Order;
import com.christinagorina.order.model.OrderEvent2;
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
                //TODO itemIds builder
                //.itemIds(orderDto.getItemIds())
                .point(orderDto.getPoint())
                .pointId(orderDto.getPointId())
                .address(orderDto.getAddress())
                .price(orderDto.getPrice())
                .build();
    }

    public OrderEvent2 orderToOrderEvent(@NonNull Order order){
        log.info("orderToOrderEvent qwe start");
        return OrderEvent2.builder()
                .uuid(order.getUuid())
                .build();

    }
}
