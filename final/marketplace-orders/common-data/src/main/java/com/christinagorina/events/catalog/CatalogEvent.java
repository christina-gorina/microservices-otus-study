package com.christinagorina.events.catalog;

import com.christinagorina.status.OrderStatus;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEvent {

    @NonNull
    private UUID orderUuid;
    private OrderStatus status;
    @NonNull
    private Double addressX;
    @NonNull
    private Double addressY;
    @NonNull
    private Map<UUID, Integer> productItemsUuidAndCount;

}
