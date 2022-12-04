package com.christinagorina.events.payment;

import com.christinagorina.status.BillingStatus;
import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingEvent {

    private Long orderId;
    private OrderStatus orderStatus;

}
