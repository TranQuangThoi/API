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
    List<Voucher> findByKinds(@Param("status") Integer status ,@Param("kinds") List<Integer> kinds );
}
