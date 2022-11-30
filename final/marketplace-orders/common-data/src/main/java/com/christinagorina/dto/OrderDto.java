package com.christinagorina.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class OrderDto {

    private UUID uuid;
    private Long  userId;
    private Map<Long, Integer> productItemsIdAndCount;
    private Boolean point;
    private Long  pointId;
    private Double addressX;
    private Double addressY;
    private BigDecimal price;

}
