package com.christinagorina.events;

import com.christinagorina.status.OrderStatus;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsEvent {

    private Long orderId;
    @NonNull
    private OrderStatus orderStatus;
    private Long userId;
    private String userMessage;
    private Map<UUID, Integer> productItemsUuidAndCount;
    private List<ReserveOnWarehouse> reserveOnWarehouses;

}
