package com.christinagorina.logistics.controller;

import com.christinagorina.logistics.model.Warehouse;
import com.christinagorina.logistics.repostory.WarehouseRepository;
import com.christinagorina.logistics.service.WarehouseCreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LogisticsController {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseCreatorService warehouseCreatorService;

    @PostMapping("/api/logisticsDbReInit")
    public List<Warehouse> logisticsDbReInit() {
        log.info("logisticsDbReInit");
        warehouseRepository.deleteAll();
        List<Warehouse> warehouseList = warehouseRepository.saveAll(warehouseCreatorService.createWarehouse());
        log.info("warehouseList = " + warehouseList);
        return warehouseList;
    }

}
