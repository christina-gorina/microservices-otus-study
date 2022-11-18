package com.christinagorina.events.catalog;

import com.christinagorina.dto.CatalogDto;
import com.christinagorina.events.Event;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CatalogEvent implements Event {

    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private CatalogDto catalog;
    private CatalogStatus status;

    public CatalogEvent() {
    }

    public CatalogEvent(CatalogDto catalog, CatalogStatus status) {
        this.catalog = catalog;
        this.status = status;
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public CatalogDto getCatalog() {
        return catalog;
    }

    public CatalogStatus getStatus() {
        return status;
    }

}
