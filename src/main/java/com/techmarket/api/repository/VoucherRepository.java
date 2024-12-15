package com.techmarket.api.repository;

import com.techmarket.api.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher,Long>, JpaSpecificationExecutor<Voucher> {

    List<Voucher> findByExpiredBeforeAndStatusEquals(Date expired, Integer status);
    @Query("SELECT v from Voucher v where v.status= :status AND v.kind IN :kinds ")
    List<Voucher> findByKindsforguest(@Param("status") Integer status ,@Param("kinds") List<Integer> kinds );
    @Query(value = "SELECT v.* " +
            "FROM db_voucher v " +
            "WHERE v.status = :status " +
            "AND v.kind IN (:kinds) " +
            "AND NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM voucher_is_used viu " +
            "    WHERE viu.voucher_id = v.id " +
            "    AND viu.user_id = :userId " +
            ")", nativeQuery = true)
    List<Voucher> findByKinds(
            @Param("status") Integer status,
            @Param("kinds") List<Integer> kinds,
            @Param("userId") Long userId
    );





}
