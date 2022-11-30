package com.christinagorina.logistics.controller;

import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.logistics.Service.WarehouseService;
import com.christinagorina.logistics.model.WarehouseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@Slf4j
public class LogisticsController {
    //TODO исправить
    private final WarehouseService warehouseService;

    @PostMapping("/api/warehouse")
    public String create(@RequestBody WarehouseDto warehouseDto) {
        log.info("warehouseDto qwe = " + warehouseDto);
        warehouseService.warehouseReserve(new CatalogEvent());
        return "WarehouseDto success";
    }



}
