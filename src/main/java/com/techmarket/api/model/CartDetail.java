package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_cart_detail")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CartDetail extends Auditable<String>{

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "product_variant")
    private ProductVariant productVariant;
}
