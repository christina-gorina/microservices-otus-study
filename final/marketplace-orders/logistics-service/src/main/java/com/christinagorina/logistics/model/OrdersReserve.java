package com.christinagorina.logistics.model;

import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ordersReserve")
public class OrdersReserve {
    @Id
    private String id;
    private UUID uuid;
    //TODO возможно для откатывающей транзакции здесь статус и товары с количеством завести
}
