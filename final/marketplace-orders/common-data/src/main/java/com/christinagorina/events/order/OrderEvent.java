package com.christinagorina.events.order;


import com.christinagorina.status.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

//TODO перенести в common и переименовать без 2
//TODO разобраться какие поля тут нужны
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    //TODO проверить остальные модели не надо ли  @NonNull
    @NonNull
    private UUID uuid;   //TODO это надо? Мб сделать таблицу с логом
    @NonNull
    private HashMap<Long, Integer> productItemsIdAndCount;
    @NonNull
    private OrderStatus orderStatus;
    @NonNull
    private Long userId;
    @NonNull
    private BigDecimal price;
}
