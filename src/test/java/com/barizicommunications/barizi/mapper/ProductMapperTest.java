package com.barizicommunications.barizi.mapper;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.models.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {
    @InjectMocks
    private ProductMapper productMapper;
    @Test
    void should_convert_product_to_dto(){
        UUID productId =  UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .name("Java Coffee")
                .description("Best coffee in kenya")
                .currentStock(100)
                .minimumStockLevel(50)
                .build();

        ProductResponse expected = ProductResponse.builder()
                .id(productId)
                .name("Java Coffee")
                .description("Best coffee in kenya")
                .currentStock(100)
                .minimumStockLevel(50)
                .build();

        ProductResponse actual = productMapper.toDto(product);

        assertEquals(actual.currentStock(),expected.currentStock());
        assertEquals(actual.minimumStockLevel(),expected.minimumStockLevel());
        assertEquals(actual.name(),expected.name());
        assertEquals(actual.description(),expected.description());
        assertEquals(actual.id(),expected.id());
    }

    @Test
    void should_convert_dto_to_product(){
        UUID productId =  UUID.randomUUID();
        Product expected = Product.builder()
                .id(productId)
                .name("Java Coffee")
                .description("Best coffee in kenya")
                .currentStock(0)
                .minimumStockLevel(50)
                .build();

        ProductRequest request = ProductRequest.builder()
                .name("Java Coffee")
                .description("Best coffee in kenya")
                .minimumStockLevel(50)
                .build();

        Product actual = productMapper.toEntity(request);

        assertEquals(actual.getMinimumStockLevel(),expected.getMinimumStockLevel());
        assertEquals(actual.getName(),expected.getName());
        assertEquals(actual.getDescription(),expected.getDescription());
        assertEquals(actual.getCurrentStock(),expected.getCurrentStock());
    }
}