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
    @Query("select od from OrderDetail od join od.order o "+
            "WHERE o.phone= :phone "+
            "AND o.id = :id")
    Page<OrderDetail> findAllByOrderIdAndPhone(String phone,long id , Pageable pageable);

    List<OrderDetail> findAllByOrderId(Long id );

    @Query("SELECT od FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.state = :state " +
            "AND od.product_Id = :productId " +
            "AND od.isReviewed = false")
    List<OrderDetail> findUnpaidOrderDetailsByUserIdAndProductId(Integer state,Long userId, Long productId);

    @Query("SELECT od.product_Id FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.state = :state " +
            "AND od.isReviewed = false")
    List<Long> findProductIdUnrated(Integer state,Long userId);

    @Query("SELECT SUM(od.price) FROM OrderDetail od "+
            "JOIN od.order o "+
            "WHERE od.product_Id= :id "+
            "AND o.state = :state "+
            "AND o.isPaid= true")
    Double calculatePriceProduct(Integer state,Long id);

}
