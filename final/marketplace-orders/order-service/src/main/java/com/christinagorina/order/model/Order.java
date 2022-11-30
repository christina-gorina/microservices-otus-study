package com.christinagorina.order.model;

import com.christinagorina.status.LockState;
import com.christinagorina.status.OrderStatus;
import com.christinagorina.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "isorder")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID orderUuid;
    private Long  userId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private Boolean point;
    private Long  pointId;
    private Double addressX;
    private Double addressY;
    private LocalDateTime dateTime;
    private BigDecimal price;
    private LockState lockState;

}
