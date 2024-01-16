package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_cart")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Cart extends Auditable<String>{

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JoinColumn(name = "total_product")
    private Integer totalProduct;
}
