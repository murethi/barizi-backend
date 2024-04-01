package com.barizicommunications.barizi.service;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.exceptions.InvalidQuantityException;
import com.barizicommunications.barizi.exceptions.NotFoundException;
import com.barizicommunications.barizi.mapper.ProductMapper;
import com.barizicommunications.barizi.models.Product;
import com.barizicommunications.barizi.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Should retrieve all products when findAll method is called
     */
    @Test
    void should_retrieve_a_list_of_all_products(){
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        ProductResponse productResponse = ProductResponse.builder()
                .id(UUID.randomUUID())
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        List<Product> products= List.of(product);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(productResponse);

        List<ProductResponse> actual = productService.findAll();

        assertEquals(actual.size(),products.size());

        verify(productRepository,times(1)).findAll();
    }
    /**
     * test that product service is able to save a product
     */
    @Test
    public void should_create_product() {
        // Given
        ProductRequest productRequest = new ProductRequest(
                "Java Coffee 500g",
                "Best coffee in Kenya",
                15
        );
        Product product = Product.builder()
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();

        Product expected = Product.builder()
                .id(UUID.randomUUID())
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();

        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(expected);

        // When
        productService.create(productRequest);

        // Then
        verify(productMapper, times(1)).toEntity(eq(productRequest));
        verify(productRepository, times(1)).save(product);
    }


    /**
     * Product service should be able to update product if exists
     */
    @Test
    public void should_update_product() {
        UUID productId = UUID.randomUUID();
        // Given
        ProductRequest productRequest = new ProductRequest(
                "Java Coffee 500g - Edited",
                "Best coffee in Kenya",
                15
        );
        Product product = Product.builder()
                .id(productId)
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        Product expected = Product.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(expected);

        // When
        productService.update(productRequest,productId);

        // Then
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productRepository, times(1)).findById(eq(productId));
    }

    /**
     * On update, an exception is thrown if product cannot be found
     */
    @Test
    void update_should_throw_exception_if_product_not_found(){
        UUID productId = UUID.randomUUID();
        ProductRequest productRequest = new ProductRequest(
                "Java Coffee 500g - Edited",
                "Best coffee in Kenya",
                15
        );
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,()->productService.update(productRequest,productId));

        assertEquals(exception.getMessage(),"Product not found");
    }

    /**
     * findOne method should retrieve product if found
     */
    @Test
    void should_retrieve_product_by_id(){
        UUID productId = UUID.randomUUID();

        Product product = Product.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();

        ProductResponse expected = ProductResponse.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(expected);

        ProductResponse actual = productService.findOne(productId);



        assertEquals(actual.currentStock(),expected.currentStock());
        assertEquals(actual.minimumStockLevel(),expected.minimumStockLevel());
        assertEquals(actual.name(),expected.name());
        assertEquals(actual.description(),expected.description());

        verify(productMapper,times(1)).toDto(any());
        verify(productRepository,times(1)).findById(any());
        verify(productRepository,times(1)).findById(eq(productId));
    }

    /**
     * find one method should throw exception with message if not found
     */
    @Test
    void retrieve_should_throw_exception_if_product_not_found(){
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,()->productService.findOne(productId));

        verify(productMapper,never()).toDto(any());
        verify(productRepository,times(1)).findById(any());
        //confirm that the id we passed is the one that was used
        verify(productRepository,atLeastOnce()).findById(eq(productId));
        assertEquals(exception.getMessage(),"Product Not Found");
    }

    /**
     * an exception should be thrown if product was not found
     */
    @Test
    void remove_from_stock_should_throw_exception_if_product_not_found(){
        UUID productId = UUID.randomUUID();
        int quantity = -10;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,()->productService.removeStock(productId,quantity));

        verify(productMapper,never()).toDto(any());
        verify(productRepository,times(1)).findById(any());
        //confirm that the id we passed is the one that was used
        verify(productRepository,atLeastOnce()).findById(eq(productId));
        assertEquals(exception.getMessage(),"Product Not Found");
    }
    /**
     * product service should remove stock successfully
     * to remove, quantity must be greater or equal to requested amount
     */
    @Test
    void should_remove_quantity_from_stock(){
        UUID productId =UUID.randomUUID();
        int quantity = -10;

        Product product = mock(Product.class);


        Product modifiedProduct = Product.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(290)
                .currentStock(1500)
                .build();

        ProductResponse expected = ProductResponse.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(290)
                .currentStock(1500)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        //to remove, quantity must be greater or equal to requested amount
        when(product.getCurrentStock()).thenReturn(Math.abs(quantity));
        when(productRepository.save(any(Product.class))).thenReturn(modifiedProduct);
        when(productMapper.toDto(modifiedProduct)).thenReturn(expected);

        ProductResponse actual  = productService.removeStock(productId,quantity);

        assertEquals(actual.currentStock(),expected.currentStock());
        assertEquals(actual.minimumStockLevel(),expected.minimumStockLevel());
        assertEquals(actual.name(),expected.name());
        assertEquals(actual.description(),expected.description());

        verify(productMapper,times(1)).toDto(modifiedProduct);
        verify(productRepository,times(1)).findById(productId);
    }

    /**
     * Remove Should throw exception if stock is low
     */
    @Test
    void remove_stock_throw_exception_if_stock_is_low(){
        UUID productId =UUID.randomUUID();
        int quantity = -10;

        Product product = mock(Product.class);



        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        //stock is low if the current stock is less than requested quantity
        when(product.getCurrentStock()).thenReturn(Math.abs(quantity)-1);

        var exception=assertThrows(NotFoundException.class,()->productService.removeStock(productId,quantity));

        assertEquals(exception.getMessage(),"Product Not Found");

        verify(productMapper,never()).toDto(any());
        verify(productRepository,times(1)).findById(productId);
        verify(productRepository,never()).save(any());
    }

    /**
     * remove from stock should fail if quantity is zero or greater
     */
    @Test
    void remove_stock_throw_exception_if_quantity_is_zero_or_greater(){
        UUID productId =UUID.randomUUID();
        int quantity = 0;

        var exception=assertThrows(InvalidQuantityException.class,()->productService.removeStock(productId,quantity));

        assertEquals(exception.getMessage(),"To remove from stock, quantity must be less than zero (0)");

        verify(productMapper,never()).toDto(any());
        verify(productRepository,never()).findById(productId);
        verify(productRepository,never()).save(any());
    }

    /**
     * product service should add stock successfully
     */
    @Test
    void should_add_quantity_to_stock(){
        UUID productId =UUID.randomUUID();
        int quantity = 10;

        Product product = mock(Product.class);


        Product modifiedProduct = Product.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(310)
                .currentStock(1500)
                .build();

        ProductResponse expected = ProductResponse.builder()
                .id(productId)
                .name("Java Coffee 500g - Edited")
                .description("Best coffee in Kenya")
                .minimumStockLevel(300)
                .currentStock(1500)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(modifiedProduct);
        when(productMapper.toDto(modifiedProduct)).thenReturn(expected);

        ProductResponse actual  = productService.addStock(productId,quantity);

        assertEquals(actual.currentStock(),expected.currentStock());
        assertEquals(actual.minimumStockLevel(),expected.minimumStockLevel());
        assertEquals(actual.name(),expected.name());
        assertEquals(actual.description(),expected.description());

        verify(productMapper,times(1)).toDto(modifiedProduct);
        verify(productRepository,times(1)).findById(productId);
    }

    /**
     * Add to stock will throw exception if product not found
     */
    @Test
    void add_to_stock_should_throw_exception_if_product_not_found(){
        UUID productId = UUID.randomUUID();
        int quantity = 10;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,()->productService.addStock(productId,quantity));

        verify(productMapper,never()).toDto(any());
        verify(productRepository,times(1)).findById(any());
        //confirm that the id we passed is the one that was used
        verify(productRepository,atLeastOnce()).findById(eq(productId));
        assertEquals(exception.getMessage(),"Product Not Found");
    }

    /**
     * add to stock will throw exception if quantity is zero or less
     */
    @Test
    void add_stock_throw_exception_if_quantity_is_zero_or_less(){
        UUID productId =UUID.randomUUID();
        int quantity = 0;

        var exception=assertThrows(InvalidQuantityException.class,()->productService.addStock(productId,quantity));

        assertEquals(exception.getMessage(),"To remove from stock, quantity must be greater than zero (0)");

        verify(productMapper,never()).toDto(any());
        verify(productRepository,never()).findById(productId);
        verify(productRepository,never()).save(any());
    }
}