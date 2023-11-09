package com.techmarket.api.repository;

import com.techmarket.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT b FROM Product b WHERE LOWER(b.name) LIKE %:name%")
    Product findByNameContainingIgnoreCase(String name);
}
