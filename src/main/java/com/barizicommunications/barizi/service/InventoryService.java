package com.barizicommunications.barizi.service;

import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.events.StockRemovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * In this particular case, the inventory service only has an add and remove method
 */
@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductService productService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ProductResponse updateStock(UUID id, int quantity) {
        if (quantity < 0) {
            ProductResponse productResponse = productService.removeStock(id, quantity);
            applicationEventPublisher.publishEvent(new StockRemovedEvent(this, productResponse));
            return productResponse;
        } else {
            return productService.addStock(id, quantity);
        }


    }

}
