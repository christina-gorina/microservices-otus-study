package com.christinagorina.order.model;

import com.christinagorina.status.OrderStatus;
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
    private String  userName;
    private OrderStatus orderStatus;
    private Double addressX;
    private Double addressY;
    private LocalDateTime dateTime;
    private BigDecimal price;
    @Column(columnDefinition="text")
    private String userMessage;

}
