package com.christinagorina.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ordersReserve")
public class OrdersIdempotent {
    @Id
    private String id;
    private Long orderId;
    //TODO возможно для откатывающей транзакции здесь статус и товары с количеством завести
}
