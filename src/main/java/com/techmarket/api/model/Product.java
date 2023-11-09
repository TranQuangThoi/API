package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_product")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Product extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.techmarket.api.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private String name;
    private Double price;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    private String image;
    private Integer totalInStock;
    private Integer totalReview;
    private Double saleOff;
    private Integer soldAmount;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
