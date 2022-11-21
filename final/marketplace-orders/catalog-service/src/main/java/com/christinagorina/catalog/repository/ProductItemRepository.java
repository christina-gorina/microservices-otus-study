package com.christinagorina.catalog.repository;

import com.christinagorina.catalog.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

}
