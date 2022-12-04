package com.christinagorina.events.order;


import com.christinagorina.status.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

//TODO перенести в common и переименовать без 2
//TODO разобраться какие поля тут нужны
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    //TODO проверить остальные модели не надо ли  @NonNull
    private Long orderId;
    private Map<UUID, Integer> productItemsUuidAndCount;
    @NonNull
    private OrderStatus orderStatus;
    private Long userId;
    private BigDecimal price;
    private Double addressX;
    private Double addressY;
}
