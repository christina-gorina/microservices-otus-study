package com.christinagorina.events.logistics;

import com.christinagorina.status.OrderStatus;
import lombok.*;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsEvent {

    @NonNull
    private Long orderId;
    @NonNull
    private OrderStatus orderStatus;

    private String isLogisticsEvent;

    private Long userId;

}
