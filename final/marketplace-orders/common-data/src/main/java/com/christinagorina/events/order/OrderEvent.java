package com.christinagorina.events.order;


import lombok.*;

import java.util.HashMap;
import java.util.UUID;

//TODO перенести в common и переименовать без 2
//TODO разобраться какие поля тут нужны
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private UUID uuid;
    private HashMap<Long, Integer> productItemsIdAndCount;
    private OrderStatus orderStatus;

}
