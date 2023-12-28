package com.techmarket.api.repository;

import com.techmarket.api.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {

    Page<OrderDetail> findAllByOrderId(long id , Pageable pageable);
    @Query("select od from OrderDetail od join od.order o "+
            "WHERE o.phone= :phone "+
            "AND o.id = :id")
    Page<OrderDetail> findAllByOrderIdAndPhone(@Param("phone") String phone, @Param("id") long id , Pageable pageable);

    List<OrderDetail> findAllByOrderId(Long id );

    @Query("SELECT od FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.state = :state " +
            "AND od.product_Id = :productId " +
            "AND od.isReviewed = false")
    List<OrderDetail> findUnpaidOrderDetailsByUserIdAndProductId(@Param("state") Integer state,@Param("userId") Long userId,@Param("productId") Long productId);
    @Query("SELECT count(od)>0 FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.state = :state " +
            "AND od.id = :orderDetailId " +
            "AND od.isReviewed = false")
    Boolean checkReviewProduct(@Param("state") Integer state,@Param("userId") Long userId ,@Param("orderDetailId") Long orderDetailId);

    @Query("SELECT od.product_Id FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.user.id = :userId " +
            "AND o.state = :state " +
            "AND od.isReviewed = false")
    List<Long> findProductIdUnrated(@Param("state") Integer state,@Param("userId") Long userId);

    @Query("SELECT od.product_Id, SUM(od.price) as totalPrice " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.state = :state " +
            "AND o.isPaid = true " +
            "GROUP BY od.product_Id "+
            "ORDER BY totalPrice DESC")
    Page<Object[]> calculatePriceProduct(@Param("state") Integer state, Pageable pageable);



}
