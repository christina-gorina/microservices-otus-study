package com.christinagorina.events;

import com.christinagorina.status.OrderStatus;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsEvent {

    private Long orderId;
    @NonNull
    private OrderStatus orderStatus;
    private Long userId;
    private String userMessage;

}
