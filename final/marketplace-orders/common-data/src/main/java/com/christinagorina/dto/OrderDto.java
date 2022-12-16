package com.christinagorina.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class OrderDto {

    private UUID orderUuid;
    private String  userName;
    private Map<UUID, Integer> productItemsUuidAndCount;
    private Boolean point;
    private Long  pointId;
    private Double addressX;
    private Double addressY;
    private BigDecimal price;

}
