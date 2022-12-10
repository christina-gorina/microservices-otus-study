package com.christinagorina.catalog.controller;

import com.christinagorina.catalog.model.ProductItem;
import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.catalog.service.ProductItemCreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CatalogController {

    private final ProductItemRepository productItemRepository;
    private final ProductItemCreatorService productItemCreatorService;

    @PostMapping("/api/catalogDbReInit")
    public List<ProductItem> catalogDbReInit() {
        log.info("catalogDbReInit");
        productItemRepository.deleteAll();
        List<ProductItem> productItemList = productItemRepository.saveAll(productItemCreatorService.createProductItem());
        log.info("productItemList = " + productItemList);
        return productItemList;
    }

}
