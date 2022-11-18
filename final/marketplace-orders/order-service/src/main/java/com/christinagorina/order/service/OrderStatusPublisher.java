package com.christinagorina.order.service;

import com.christinagorina.events.order.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {

    @Autowired
    private Sinks.Many<OrderEvent> orderSink;

    public void raiseOrderEvent(){

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setLastName("LastName");
        orderEvent.setName("Name");

        this.orderSink.tryEmitNext(orderEvent);
    }

}
