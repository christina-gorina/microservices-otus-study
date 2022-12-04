package com.christinagorina.catalog.model;

import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders_idempotent")
@Builder
public class OrdersIdempotent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private OrderStatus orderStatus;
    //TODO возможно для откатывающей транзакции здесь статус и товары с количеством завести
}
