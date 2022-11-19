package com.christinagorina.catalog.service;

import com.christinagorina.events.order.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {


    @Transactional
    public void cancelOrderInventory(OrderEvent orderEvent){

    }

}
