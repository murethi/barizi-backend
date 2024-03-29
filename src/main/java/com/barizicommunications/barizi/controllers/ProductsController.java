package com.barizicommunications.barizi.controllers;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;

    /**
     * fetch all products from db
     * @return List
     */
    @GetMapping
    public List<ProductResponse> index(){
        return productService.findAll();
    }

    /**
     * The void return method was chosen as per https://www.rfc-editor.org/rfc/rfc7231#section-4.3
     * In some cases we may return the created resource for the client's immediate use
     * @param productRequest
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductRequest productRequest){
        productService.create(productRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@RequestBody ProductRequest productRequest, @PathVariable UUID id){
        productService.update(productRequest,id);
    }
}
