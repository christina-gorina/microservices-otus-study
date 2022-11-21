package com.christinagorina.catalog.service;

import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.events.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductItemRepository productItemRepository;

    @Transactional
    public CatalogEvent productItemReservation(OrderEvent orderEvent) {
        log.info("productItemReservation orderEvent qwe = " + orderEvent);

        Optional.ofNullable(orderEvent)
                .map(OrderEvent::getProductItemsIdAndCount)
                .orElseThrow(IllegalArgumentException::new)
                .forEach(this::makeReservation);


        return new CatalogEvent();
    }

    @Transactional
    public void cancelProductItemReservation(OrderEvent orderEvent) {

    }

    private void makeReservation(Long id, Integer count) {
        //TODO Остановилась тут, сделать резервирование, не забыть про изоляцию транзакции при рещзервировании, нкжен REPEATABLE_READ
        //Todo можно этот момент рассказать на защите
        log.info("makeReservation id qwe = " + id);
        log.info("makeReservation count qwe = " + count);

        productItemRepository.findById(id)
                .filter(productItem -> productItem.getCount() > 0 )
                .map(productItem -> {
                    productItem.setCount(productItem.getCount() - 1);
                    // TODO проверить, но кажется тут забыли сохранить productItem
                    consumptionRepository.save(OrderInventoryConsumption.of(orderEvent.getPurchaseOrder().getOrderId(), orderEvent.getPurchaseOrder().getProductId(), 1));
                    return new InventoryEvent(dto, InventoryStatus.RESERVED);
                })
                .orElse(new InventoryEvent(dto, InventoryStatus.REJECTED));
    }

}
