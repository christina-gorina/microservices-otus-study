package com.christinagorina.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveOnWarehouse {
    private UUID productItemUUID;
    private Integer count;
    private String warehouseName;
}
