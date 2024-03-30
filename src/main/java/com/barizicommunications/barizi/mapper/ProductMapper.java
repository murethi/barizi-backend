package com.barizicommunications.barizi.mapper;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.models.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductMapper {

    public Function<Product, ProductResponse> toDto = product->{
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .currentStock(product.getCurrentStock())
                .minimumStockLevel(product.getMinimumStockLevel())
                .build();
    };

    public Function<ProductRequest, Product> toEntity = productRequest->{
        Product product = Product.builder()
                .build();
        //by using bean utils, I am able to copy all properties from the request object to the entity
        BeanUtils.copyProperties(productRequest,product);
        return product;
    };

}
