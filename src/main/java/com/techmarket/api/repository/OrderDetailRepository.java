package com.techmarket.api.repository;

import com.techmarket.api.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {

    Page<OrderDetail> findAllByOrderId(long id , Pageable pageable);

    @Query("SELECT od FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.isPaid = true " +
            "AND od.product_Id = :productId " +
            "AND od.isReviewed = false")
    List<OrderDetail> findUnpaidOrderDetailsByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT od.product_Id FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.isPaid = true " +
            "AND od.isReviewed = false")
    List<Long> findProductIdUnrated(Long userId);


}
