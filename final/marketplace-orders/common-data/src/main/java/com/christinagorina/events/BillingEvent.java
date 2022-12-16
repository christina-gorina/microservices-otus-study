package com.christinagorina.events;

import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingEvent {

    private Long orderId;
    private OrderStatus orderStatus;
    private String userMessage;
    private List<ReserveOnWarehouse> reserveOnWarehouses;
    private Map<UUID, Integer> productItemsUuidAndCount;

}
