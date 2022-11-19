package com.christinagorina.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDto {

    public String uuid;
    public Long  userId;
    public List<Long> itemIds;
    public Boolean point;
    public Long  pointId;
    public String address;
    public BigDecimal price;

}
