package com.techmarket.api.repository;

import com.techmarket.api.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Category findByName(String name);
    @Query("Select distinct p.category FROM Product p " +
            "join p.brand b " +
            "where b.id=:brandId")
    Page<Category> listCategoryByBrandId(@Param("brandId") Long brandId, Pageable pageable);
}
