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
@Table(name = "idempotency_storage")
@Builder
public class IdempotencyStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "idempotency_key")
    public String idempotencyKey;

    @Column(name = "order_id")
    public Long orderId;

    @Column(name = "creation_date_time")
    public LocalDateTime creationDateTime;

}