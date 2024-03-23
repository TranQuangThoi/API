package com.techmarket.api.model;

import com.techmarket.api.validation.RefundsState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_refunds")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Refunds extends Auditable<String>{

    @OneToOne
    @JoinColumn(name = "order_Id")
    private Order order;
    @RefundsState
    private Integer state;
    private Boolean cash;
    private String bank;
    @Column(name = "account_number")
    private String accountNumber;
    private Double fee;
    private String phone;
    private String name;
}
