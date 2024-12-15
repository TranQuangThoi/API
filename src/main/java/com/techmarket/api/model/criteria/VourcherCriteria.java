package com.techmarket.api.model.criteria;

import com.techmarket.api.model.Account;
import com.techmarket.api.model.User;
import com.techmarket.api.model.Voucher;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class VourcherCriteria {

    private Long id;
    private String title;
    private String content;
    private String percent;
    private Date expired;
    private Integer kind;
    private Integer status;

    public Specification<Voucher> getSpecification() {
        return new Specification<Voucher>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId()!=null)
                {
                    predicates.add(cb.equal(root.get("id"),getId()));
                }
                if(getStatus()!=null)
                {
                    predicates.add(cb.equal(root.get("status"),getStatus()));
                }
                if(getKind()!=null)
                {
                    predicates.add(cb.equal(root.get("kind"),getKind()));
                }
                if (!StringUtils.isBlank(getTitle()))
                {
                    predicates.add(cb.like(cb.lower(root.get("title")),"%"+ getTitle()+"%"));
                }
                if (!StringUtils.isBlank(getContent()))
                {
                    predicates.add(cb.like(cb.lower(root.get("content")),"%"+ getContent()+"%"));
                }
                if(getPercent()!=null)
                {
                    predicates.add(cb.equal(root.get("percent"),getPercent()));
                }
                if(getExpired()!=null)
                {
                    predicates.add(cb.equal(cb.function("date", Date.class, root.get("expired")), getExpired()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
