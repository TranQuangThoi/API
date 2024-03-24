package com.techmarket.api.repository;

import com.techmarket.api.model.Refunds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RefundsRepository extends JpaRepository<Refunds, Long>, JpaSpecificationExecutor<Refunds> {

    Refunds findByOrderId(Long orderId);
}
