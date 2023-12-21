package com.techmarket.api.repository;

import com.techmarket.api.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartDetailRepository  extends JpaRepository<CartDetail, Long>, JpaSpecificationExecutor<CartDetail> {

    List<CartDetail> findAllByCartId(Long cartId);

    @Transactional
    void deleteAllByCartId(Long cartId);
    List<CartDetail> findAllByProductVariantId(Long id);
    @Transactional
    void deleteAllByProductVariantId(Long id);
}
