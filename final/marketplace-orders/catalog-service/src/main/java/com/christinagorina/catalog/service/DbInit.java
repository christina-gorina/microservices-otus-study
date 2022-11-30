package com.christinagorina.catalog.service;

import com.christinagorina.catalog.model.ProductItem;
import com.christinagorina.catalog.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbInit {

    @Autowired
    private ProductItemRepository productItemRepository;

    @PostConstruct
    private void postConstruct() {
        ProductItem productItem1 = new ProductItem(1L, 10, "productItem1");
        ProductItem productItem2 = new ProductItem(2L, 12, "productItem2");
        ProductItem productItem3 = new ProductItem(3L, 3, "productItem3");
        ProductItem productItem4 = new ProductItem(4L, 37, "productItem4");
        ProductItem productItem5 = new ProductItem(5L, 6, "productItem5");
        ProductItem productItem6 = new ProductItem(6L, 18, "productItem6");
        ProductItem productItem7 = new ProductItem(7L, 14, "productItem7");
        ProductItem productItem8 = new ProductItem(8L, 30, "productItem8");
        ProductItem productItem9 = new ProductItem(9L, 24, "productItem9");
        ProductItem productItem10 = new ProductItem(10L, 15, "productItem10");

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
        productItemList.add(productItem10);

        productItemRepository.saveAll(productItemList);
    }
}