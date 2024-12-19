package com.techmarket.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "db_voucher")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Voucher extends Auditable<String>{

    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private Integer percent;
    private Date expired;
    private Integer kind;
    private Integer amount;
    private Integer priceMax;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "voucher_is_used",
            joinColumns = @JoinColumn(name = "voucher_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"))
    private List<User> is_used = new ArrayList<>();

}
