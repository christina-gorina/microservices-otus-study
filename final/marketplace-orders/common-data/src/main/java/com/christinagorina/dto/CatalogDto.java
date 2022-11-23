package com.christinagorina.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDto {
//TODO вынести общие зависимости типа кафки в главный pom
    private UUID orderId;
    private Integer productId;

}
