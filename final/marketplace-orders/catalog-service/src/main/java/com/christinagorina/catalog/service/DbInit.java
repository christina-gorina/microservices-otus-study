package com.christinagorina.catalog.service;

import com.christinagorina.catalog.model.ProductItem;
import com.christinagorina.catalog.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DbInit {

    @Autowired
    private ProductItemRepository productItemRepository;

    @PostConstruct
    private void postConstruct() {
        ProductItem productItem1 = new ProductItem(1L, UUID.fromString("16cb7a21-eb5e-4d41-a176-8f49ca425b0c"), 23, "productItem1", new BigDecimal(6));
        ProductItem productItem2 = new ProductItem(2L, UUID.fromString("261e9635-3ed0-485f-8f28-56aa156958ea"), 45, "productItem2", new BigDecimal(3));
        ProductItem productItem3 = new ProductItem(3L, UUID.fromString("3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8"), 19, "productItem3", new BigDecimal(4));
        ProductItem productItem4 = new ProductItem(4L, UUID.fromString("44c053f4-f408-4193-906a-4270e978c70c"), 40, "productItem4", new BigDecimal(8));
        ProductItem productItem5 = new ProductItem(5L, UUID.fromString("5188d680-7dfc-4f5a-b6bb-04d7ca14486c"), 30, "productItem5", new BigDecimal(5));
        ProductItem productItem6 = new ProductItem(6L, UUID.fromString("6c6f53c7-d100-4946-a161-012c49028905"), 30, "productItem6", new BigDecimal(7));
        ProductItem productItem7 = new ProductItem(7L, UUID.fromString("793a5f4e-8179-44ca-a98b-e47fc8e1ae35"), 28, "productItem7", new BigDecimal(4));
        ProductItem productItem8 = new ProductItem(8L, UUID.fromString("8af6b13d-d72f-4612-89c0-2aa0a09524b0"), 15, "productItem8", new BigDecimal(5));
        ProductItem productItem9 = new ProductItem(9L, UUID.fromString("95e028b4-c360-4143-b299-ce9931c5adc0"), 10, "productItem9", new BigDecimal(2));

        List<ProductItem> productItemList = new ArrayList<>();
        productItemList.add(productItem1);
        productItemList.add(productItem2);
        productItemList.add(productItem3);
        productItemList.add(productItem4);
        productItemList.add(productItem5);
        productItemList.add(productItem6);
        productItemList.add(productItem7);
        productItemList.add(productItem8);
        productItemList.add(productItem9);

        productItemRepository.saveAll(productItemList);
    }
}