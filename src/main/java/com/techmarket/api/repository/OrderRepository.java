package com.techmarket.api.repository;

import com.techmarket.api.dto.revenue.RevenueDto;
import com.techmarket.api.dto.revenue.RevenueOfYearDto;
import com.techmarket.api.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Page<Order> findAllByUserId(Long id , Pageable pageable);
    List<Order> findAllByUserId(Long id);
    @Query("SELECT NEW com.techmarket.api.dto.revenue.RevenueDto(sum (od.totalMoney) , count (od)) " +
            "FROM Order od " +
            "where  od.state= :state")
    RevenueDto countAndSumRevenueTotal(@Param("state") Integer state);
    @Query("SELECT NEW com.techmarket.api.dto.revenue.RevenueDto(sum (od.totalMoney) , count (od)) " +
            "FROM Order od " +
            "where  od.state= :state "+
            "AND od.createdDate BETWEEN :startDate AND :endDate")
    RevenueDto countAndSumRevenueByDate(@Param("state") Integer state,@Param("startDate") Date startDate ,@Param("endDate") Date endDate);
    @Query("SELECT NEW com.techmarket.api.dto.revenue.RevenueOfYearDto(SUM(od.totalMoney), MONTH(od.createdDate)) " +
            "FROM Order od " +
            "WHERE od.state = :state " +
            "AND YEAR(od.createdDate) = :year " +
            "GROUP BY MONTH(od.createdDate)")
    List<RevenueOfYearDto> calculateYearRevenue(@Param("state") Integer state,@Param("year") Integer year);

    @Query("SELECT od FROM Order od where od.phone= :phone ORDER BY od.createdDate DESC")
    Page<Order> findAllByPhone(@Param("phone") String phone,Pageable pageable);

    Order findByPhoneAndId(String phone ,Long id);
}
