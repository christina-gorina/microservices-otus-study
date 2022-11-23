package com.christinagorina.catalog.service;

import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.status.CatalogStatus;
import com.christinagorina.events.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductItemRepository productItemRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CatalogEvent productItemReservation(OrderEvent orderEvent) {
        //Todo можно этот момент рассказать на защите
        boolean checkCountCorrect = orderEvent.getProductItemsIdAndCount().entrySet().stream()
                .allMatch(e -> checkCount(e.getKey(), e.getValue()));
        if (!checkCountCorrect) {
            log.info("productItemReservation state REJECTED");
            return CatalogEvent.builder().status(CatalogStatus.REJECTED).build();
        }
        orderEvent.getProductItemsIdAndCount().forEach(this::makeReservation);
        log.info("productItemReservation state RESERVED");
        return CatalogEvent.builder().status(CatalogStatus.RESERVED).build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void cancelProductItemReservation(OrderEvent orderEvent) {
        orderEvent.getProductItemsIdAndCount().forEach(this::cancelReservation);
    }

    private Boolean cancelReservation(Long id, Integer count) {
        return productItemRepository.findById(id)
                .map(productItem -> {
                    productItem.setCommonCount(productItem.getCommonCount() + count);
                    return productItem;
                }).isPresent();
    }

    private Boolean checkCount(Long id, Integer count) {
        return productItemRepository.findById(id)
                .filter(productItem -> productItem.getCommonCount() - count > 0)
                .isPresent();
    }

    private Boolean makeReservation(Long id, Integer count) {
        return productItemRepository.findById(id)
                .map(productItem -> {
                    productItem.setCommonCount(productItem.getCommonCount() - count);
                    return productItem;
                }).isPresent();
    }

}
