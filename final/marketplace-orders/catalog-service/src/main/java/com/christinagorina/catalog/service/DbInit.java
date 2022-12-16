package com.christinagorina.catalog.service;

import com.christinagorina.catalog.repository.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbInit {

    private final ProductItemRepository productItemRepository;
    private final ProductItemCreatorService productItemCreatorService;

    @PostConstruct
    private void postConstruct() {
        log.info("postConstruct createProductItem");
        productItemRepository.saveAll(productItemCreatorService.createProductItem());
    }


}