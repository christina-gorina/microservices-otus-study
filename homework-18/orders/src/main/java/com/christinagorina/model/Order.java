package com.christinagorina.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "order_dentifier")
    public String orderIdentifier;

    @Column(name = "state")
    public State state;

    @Column(name = "buyer_id")
    public Long buyerId;

    @Column(name = "product")
    public String product;

    @Column(name = "creation_date_time")
    public LocalDateTime creationDateTime;

}
