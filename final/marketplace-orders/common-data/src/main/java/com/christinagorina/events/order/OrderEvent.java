package com.christinagorina.events.order;


import com.christinagorina.events.Event;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class OrderEvent implements Event {

    private String name;
    private String lastName;

    public OrderEvent() {
    }

    @Override
    public UUID getEventId() {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }
}
