package com.techmarket.api.repository;

import com.techmarket.api.model.Product;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT b FROM Product b WHERE LOWER(b.name) LIKE %:name%")
    Product findByNameContainingIgnoreCase(@Param("name") String name);

    @Transactional
    void deleteAllByCategoryId(Long id);
    @Transactional
    void deleteAllByBrandId(Long id);
    List<Product> findAllByCategoryId(Long id);
    List<Product> findAllByBrandId(Long id);

    @Query("SELECT p FROM Product p ORDER BY p.soldAmount DESC")
    List<Product> findTop10ProductsBySoldAmount(Pageable pageable);

}
