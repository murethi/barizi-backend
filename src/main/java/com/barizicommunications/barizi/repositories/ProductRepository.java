package com.barizicommunications.barizi.repositories;

import com.barizicommunications.barizi.models.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findAll();
}
