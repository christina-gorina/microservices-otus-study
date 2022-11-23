package com.christinagorina.events.catalog;

import com.christinagorina.status.CatalogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEvent {

    private final UUID eventId = UUID.randomUUID(); //TODO это надо?
    private CatalogStatus status;

}
