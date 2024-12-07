package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "db_product_variant")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ProductVariant extends Auditable<String>{
    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private Double price;
    private String color;
    private Integer totalStock;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
