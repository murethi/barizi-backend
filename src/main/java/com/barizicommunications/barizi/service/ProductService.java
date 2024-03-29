package com.barizicommunications.barizi.service;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.mapper.ProductMapper;
import com.barizicommunications.barizi.models.Product;
import com.barizicommunications.barizi.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> findAll(){
        return productRepository.findAll().stream().map(productMapper.toDto).toList();
    }

    public void create(ProductRequest productRequest) {
        Product product = productMapper.toEntity.apply(productRequest);

        productRepository.save(product);
    }

    public void update(ProductRequest productRequest, UUID id) {
        productRepository.findById(id).ifPresentOrElse(product -> {
            product.setName(productRequest.name());
            product.setName(productRequest.description());
            productRepository.save(product);
        },()->{
            throw new EntityNotFoundException("Product not founf");
        });
    }
}
