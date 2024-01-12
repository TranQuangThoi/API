package com.techmarket.api.repository;

import com.techmarket.api.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartDetailRepository  extends JpaRepository<CartDetail, Long>, JpaSpecificationExecutor<CartDetail> {

    List<CartDetail> findAllByCartId(Long cartId);

    @Transactional
    void deleteAllByCartId(Long cartId);
    List<CartDetail> findAllByProductVariantId(Long id);
    @Transactional
    void deleteAllByProductVariantId(Long id);

    CartDetail findByProductVariantIdAndCartId(Long id,Long cartId);
    Integer countCartDetailByCartId(Long id);

    @Modifying
    @Query("DELETE FROM CartDetail od " +
            "WHERE od.productVariant IN " +
            "(SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId)")
    void deleteAllByProduct(@Param("productId") Long productId);
}
