package com.christinagorina.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Data
public class OrderDto {

    private UUID uuid;
    private Long  userId;
    private HashMap<Long, Integer> productItemsIdAndCount;
    private Boolean point;
    private Long  pointId;
    private String address;
    private BigDecimal price;

}
