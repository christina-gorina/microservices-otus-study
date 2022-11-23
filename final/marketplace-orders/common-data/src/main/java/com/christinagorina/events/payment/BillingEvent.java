package com.christinagorina.events.payment;

import com.christinagorina.status.BillingStatus;
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

    private final UUID eventId = UUID.randomUUID(); //TODO это надо?
    private BillingStatus status;

}
