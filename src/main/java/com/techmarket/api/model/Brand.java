package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_brand")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Brand extends Auditable<String>{

    private String name;
    private String image;
}
