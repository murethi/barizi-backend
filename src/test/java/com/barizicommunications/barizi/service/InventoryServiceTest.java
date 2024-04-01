package com.barizicommunications.barizi.service;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.events.StockRemovedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private ProductService productService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void should_add_to_stock() {
        int quantity = 10;
        UUID id = UUID.randomUUID();
        ProductResponse productResponse = new ProductResponse(
                id,
                "Java Coffee 500g",
                "Best coffee in Kenya",
                20,
                15
        );
        //Mock the calls
        when(productService.addStock(id,quantity)).thenReturn(productResponse);

        ProductResponse actual = inventoryService.updateStock(id,quantity);

        verify(productService,times(1)).addStock(eq(id),eq(quantity));
        //remove stock should not be with whichever params
        verify(productService,never()).removeStock(any(UUID.class),anyInt());
        verify(applicationEventPublisher,never()).publishEvent(any(StockRemovedEvent.class));

        assertEquals(actual.currentStock(),productResponse.currentStock());
        assertEquals(actual.minimumStockLevel(),productResponse.minimumStockLevel());
        assertEquals(actual.name(),productResponse.name());
        assertEquals(actual.description(),productResponse.description());

        assertThat(actual).isEqualTo(productResponse);

    }

    @Test
    void should_remove_from_stock() {
        int quantity = -10;
        UUID id = UUID.randomUUID();
        ProductResponse productResponse = new ProductResponse(
                id,
                "Java Coffee 500g",
                "Best coffee in Kenya",
                10,
                15
        );
        //Mock the calls
        when(productService.removeStock(id,quantity)).thenReturn(productResponse);
        doNothing().when(applicationEventPublisher).publishEvent(any(StockRemovedEvent.class));

        ProductResponse actual = inventoryService.updateStock(id,quantity);

        //confirm that the right methods are invoked
        //verify the method wont attempt to remove from stock twice
        verify(productService,times(1)).removeStock(eq(id),eq(quantity));
        //since quantity is negative, add stock should never be called
        verify(productService,never()).addStock(eq(id),eq(quantity));
        //an event should be published
        verify(applicationEventPublisher,times(1)).publishEvent(any(StockRemovedEvent.class));

        assertEquals(actual.currentStock(),productResponse.currentStock());
        assertEquals(actual.minimumStockLevel(),productResponse.minimumStockLevel());
        assertEquals(actual.name(),productResponse.name());
        assertEquals(actual.description(),productResponse.description());

        assertThat(actual).isEqualTo(productResponse);

    }
}