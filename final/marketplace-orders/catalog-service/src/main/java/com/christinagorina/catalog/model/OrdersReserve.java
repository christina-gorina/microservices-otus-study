package com.christinagorina.catalog.model;

import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders_reserve")
@Builder
public class OrdersReserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    private OrderStatus orderStatus;
    //TODO возможно для откатывающей транзакции здесь статус и товары с количеством завести
}
