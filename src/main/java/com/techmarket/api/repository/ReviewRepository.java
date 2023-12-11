package com.techmarket.api.repository;

import com.techmarket.api.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    Review findByIdAndStatus(Long id , Integer status);

    Page<Review> findAllByProductId(Long id, Pageable pageable);
    Page<Review> findAllByProductIdAndStatus(Long id, Integer status, Pageable pageable);
    Page<Review> findAllByUserId(Long id,Pageable pageable);
    @Transactional
    void deleteAllByProductId(Long id);
    @Transactional
    void deleteAllByUserId(Long id);

    @Query("SELECT AVG(rv.star) from Review rv where rv.product.id = :productId")
    Double avgStartOfProduct(@Param("productId") Long productId);
    @Query("SELECT count (rv) from Review rv where rv.product.id = :productId")
    Long countReviewOfProduct(@Param("productId") Long productId);

}
