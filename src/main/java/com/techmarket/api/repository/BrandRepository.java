package com.techmarket.api.repository;

import com.techmarket.api.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

    @Query("SELECT b FROM Brand b WHERE LOWER(b.name) LIKE %:name%")
    Brand findByNameContainingIgnoreCase(String name);

    Brand findByName(String name);

}
