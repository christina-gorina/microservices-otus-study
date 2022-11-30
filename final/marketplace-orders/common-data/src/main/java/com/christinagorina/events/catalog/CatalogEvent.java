package com.christinagorina.events.catalog;

import com.christinagorina.status.CatalogStatus;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEvent {

    @NonNull
    private UUID orderuuid;
    private CatalogStatus status;
    @NonNull
    private Double addressX;
    @NonNull
    private Double addressY;
    @NonNull
    private Map<Long, Integer> productItemsIdAndCount;

}
