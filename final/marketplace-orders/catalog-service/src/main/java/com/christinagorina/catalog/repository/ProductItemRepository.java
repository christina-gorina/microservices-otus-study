package com.christinagorina.catalog.repository;

import com.christinagorina.catalog.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    Optional<ProductItem> findByUuid(UUID uuid);

}
