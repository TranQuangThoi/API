package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "db_voucher")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Voucher extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.techmarket.api.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private Integer percent;
    private Date expired;
    private Integer kind;
    private Integer amount;
}
