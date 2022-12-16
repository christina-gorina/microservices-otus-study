package com.christinagorina.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "warehouses")
public class Warehouse {

    @Id
    private String id;
    private String name;
    private Location location;
    private List<ProductItem> productItems;
    private List<Reserve> reserveList;
}
