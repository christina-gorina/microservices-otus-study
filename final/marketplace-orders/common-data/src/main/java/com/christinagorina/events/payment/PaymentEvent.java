package com.christinagorina.events.payment;

import com.christinagorina.dto.PaymentDto;
import com.christinagorina.events.Event;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PaymentEvent implements Event {

    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private PaymentDto payment;
    private PaymentStatus paymentStatus;

    public PaymentEvent() {
    }

    public PaymentEvent(PaymentDto payment, PaymentStatus status) {
        this.payment = payment;
        this.paymentStatus = status;
    }


    public PaymentDto getPayment() {
        return payment;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
