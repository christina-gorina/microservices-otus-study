package com.christinagorina.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "isorder")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String uuid;
    public Long  userId;
    public OrderStatus state;
    public PaymentStatus paymentStatus;
    //TODO разобраться с item
    //public List<Long> itemIds;
    public Boolean point;
    public Long  pointId;
    public String address;
    public LocalDateTime dateTime;
    public BigDecimal price;

}
