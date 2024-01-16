package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_order_detail")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class OrderDetail extends Auditable<String>{

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "product_variant_id")
    private Long productVariantId;
    private String name;
    private String color;
    private Integer amount;
    private Double price;
    private Long product_Id;
    private Boolean isReviewed=false;
    private String image;

}
