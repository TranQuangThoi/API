package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_notification")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Notification extends Auditable<String>{

    @Column(name = "idUser")
    private Long idUser;
    @Column(name = "ref_id")
    private String refId;
    private String kind;
    private Boolean isRead = false;
    @Column(columnDefinition = "longtext")
    private String msg;
}
