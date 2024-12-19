package com.techmarket.api.repository;

import com.techmarket.api.model.Images;
import com.techmarket.api.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long>, JpaSpecificationExecutor<Images> {

    List<Images> findAllByProductVariantId(Long id);

    @Modifying
    @Query("DELETE FROM Images i WHERE i.productVariant IN :productVariants")
    void deleteAllByProductVariants(@Param("productVariants") List<ProductVariant> productVariants);

    @Transactional
    @Query("DELETE FROM Images i WHERE i.productVariant.id IN (SELECT pv.id FROM ProductVariant pv WHERE pv.product.id = :productId)")
    void deleteAllByProductId(@Param("productId") Long productId);



}
