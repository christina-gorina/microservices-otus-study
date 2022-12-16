package com.christinagorina.logistics.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "productItem")
public class ProductItem {

    @Id
    private String id;
    @NonNull
    private UUID uuid;

    private String name;
    @NonNull
    private Integer warehouseCount;

}
