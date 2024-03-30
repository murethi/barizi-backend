package com.barizicommunications.barizi.events.handler;

import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.events.StockRemovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryEventsListener{
    @EventListener
    @Async
    public void onStockRemovedEvent(StockRemovedEvent event) {
        ProductResponse productResponse = event.getProduct();
        if(productResponse.currentStock()<productResponse.minimumStockLevel()){
            log.warn("Stock level too low for {}",productResponse.name());
        }
    }
}
