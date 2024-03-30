package com.barizicommunications.barizi.events;

import com.barizicommunications.barizi.dto.response.ProductResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StockRemovedEvent extends ApplicationEvent {
    private ProductResponse product;
    public StockRemovedEvent(Object source, ProductResponse product) {
        super(source);
        this.product = product;
    }
}
