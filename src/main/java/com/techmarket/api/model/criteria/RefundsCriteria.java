package com.techmarket.api.model.criteria;

import com.techmarket.api.model.Refunds;
import com.techmarket.api.model.Review;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RefundsCriteria {

    private Long id;
    private Long orderId;
    private Integer state;
    private Date startDate;
    private Date endDate;


    public Specification<Refunds> getCriteria() {
        return new Specification<Refunds>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Refunds> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(getState() != null){
                    predicates.add(cb.equal(root.get("state"),getState()));
                }
                if(getOrderId() !=null)
                {
                    predicates.add(cb.equal(root.get("order").get("id"),getOrderId()));
                }
                if(getStartDate()!=null && getEndDate()!=null)
                {
                    predicates.add(cb.between(root.get("modifiedDate"),getStartDate(),getEndDate()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
