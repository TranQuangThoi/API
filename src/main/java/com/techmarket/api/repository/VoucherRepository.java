package com.techmarket.api.repository;

import com.techmarket.api.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher,Long>, JpaSpecificationExecutor<Voucher> {

    List<Voucher> findByExpiredBeforeAndStatusEquals(Date expired, Integer status);
}
