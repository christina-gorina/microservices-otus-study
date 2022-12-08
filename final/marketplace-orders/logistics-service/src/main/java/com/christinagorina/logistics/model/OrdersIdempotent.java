package com.christinagorina.logistics.model;

import com.christinagorina.status.OrderStatus;
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
    private OrderStatus orderStatus;
}
