package com.techmarket.api.repository;

import com.techmarket.api.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    public Account findAccountByUsername(String username);
    public Account findAccountByEmail(String email);
    public Account findAccountByPhoneAndStatus(String phone,Integer status);
    public Account findAccountByPhone(String phone);
    public Account findAccountByResetPwdCode(String resetPwdCode);
    public Account findAccountByEmailOrUsername(String email, String username);
    public Page<Account> findAllByKind(int kind, Pageable pageable);
}
