package com.barizicommunications.barizi.mapper;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.models.Product;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ProductMapper {

    public ProductResponse toDto (Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .currentStock(product.getCurrentStock())
                .minimumStockLevel(product.getMinimumStockLevel())
                .build();
    };

    public Product toEntity(ProductRequest productRequest){
        Product product = Product.builder()
                .build();
        //by using bean utils, I am able to copy all properties from the request object to the entity
        BeanUtils.copyProperties(productRequest,product);
        return product;
    };

}
