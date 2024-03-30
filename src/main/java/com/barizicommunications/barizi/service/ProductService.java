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

    /**
     * checks if product with given id exists
     * if yes, updates the name and description
     * if not throws an exception
     * @param productRequest dto object
     * @param id pk on db
     */
    public void update(ProductRequest productRequest, UUID id) {
        productRepository.findById(id).ifPresentOrElse(product -> {
            product.setName(productRequest.name());
            product.setName(productRequest.description());
            productRepository.save(product);
        },()->{
            throw new EntityNotFoundException("Product not found");
        });
    }

    /**
     * check if product exists by id
     * if yes maps product to dto and returns rresult
     * if no throws exception
     * @param id unique identifier (pk on db)
     * @return ProductResponse
     */
    public ProductResponse findOne(UUID id) {
        return productRepository.findById(id)
                .map(productMapper.toDto)
                .orElseThrow(()-> new EntityNotFoundException("Product Not Found"));
    }
}
