package com.christinagorina.events;

import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingEvent {

    private Long orderId;
    private OrderStatus orderStatus;
    private String userMessage; //TODO записать сообщение в случае нехватки товаров и денег

}
