package com.christinagorina.logistics.model;

import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reserve")
public class Reserve {

    @Id
    private UUID id;
    private Long orderId;
    private UUID productItemUUID;
    private Integer count;
    private OrderStatus orderStatus;
    private String warehouseName;

}
