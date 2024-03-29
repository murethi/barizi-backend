package com.barizicommunications.barizi.mapper;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.models.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductMapper {

    public Function<Product, ProductResponse> toDto = product->{
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    };

    public Function<ProductRequest, Product> toEntity = productRequest->{
        return Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .build();
    };

}
