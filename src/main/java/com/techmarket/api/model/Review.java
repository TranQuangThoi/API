package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_review")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Review extends Auditable<String>{

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.techmarket.api.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    private Integer star;

    @Column(columnDefinition = "text")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Long orderDetail;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Review parentId;
}
