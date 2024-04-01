package com.barizicommunications.barizi.service;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.exceptions.InvalidQuantityException;
import com.barizicommunications.barizi.exceptions.NotFoundException;
import com.barizicommunications.barizi.mapper.ProductMapper;
import com.barizicommunications.barizi.models.Product;
import com.barizicommunications.barizi.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> findAll(){
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    public void create(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
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
            BeanUtils.copyProperties(productRequest,product);
            productRepository.save(product);
        },()->{
            throw new NotFoundException("Product not found");
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
                .map(productMapper::toDto)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
    }

    /**
     * Transactional annotation has been used in order to ensure that only
     * one instance updates the balance account at a time to avoid lost updates.
     *
     * The first map will update our stock
     * The second map will convert the new product object to dto
     *
     * @param id pk
     * @param quantity amount to be debited
     * @return ProductResponse
     */
    @Transactional
    public ProductResponse removeStock(UUID id, int quantity) {
        if(quantity>=0){
            throw new InvalidQuantityException("To remove from stock, quantity must be less than zero (0)");
        }
        return productRepository.findById(id)
                .filter(product -> product.getCurrentStock()>=Math.abs(quantity))
                .map(product -> {
                    int currentStock = product.getCurrentStock();
                    product.setCurrentStock(currentStock+quantity);
                    return productRepository.save(product);
                })
                .map(productMapper::toDto)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
    }
    /**
     * Transactional annotation has been used in order to ensure that only
     * one instance updates the balance account at a time to avoid lost updates.
     *
     * The first map will update our stock
     * The second map will convert the new product object to dto
     *
     * @param id pk
     * @param quantity amount to be credited
     * @return ProductResponse
     */
    @Transactional
    public ProductResponse addStock(UUID id, int quantity) {
        if(quantity<=0){
            throw new InvalidQuantityException("To remove from stock, quantity must be greater than zero (0)");
        }
        return productRepository.findById(id)
                .map(product -> {
                    int currentStock = product.getCurrentStock();
                    product.setCurrentStock(currentStock+quantity);
                    return productRepository.save(product);
                })
                .map(productMapper::toDto)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
    }
}
