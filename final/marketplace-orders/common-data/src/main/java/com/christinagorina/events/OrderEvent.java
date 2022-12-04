package com.christinagorina.events;


import com.christinagorina.status.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Map<UUID, Integer> productItemsUuidAndCount;
    @NonNull
    private OrderStatus orderStatus;
    private Long userId;
    private BigDecimal price;
    private Double addressX;
    private Double addressY;
}
