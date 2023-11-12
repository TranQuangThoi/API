package com.techmarket.api.model.criteria;

import com.techmarket.api.model.Review;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewCriteria {
    private Long id;
    private Integer star;
    private Long userId;
    private Long productId;
    private Integer status;
    private String message;

    public Specification<Review> getCriteria() {
        return new Specification<Review>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(getUserId() != null){
                    predicates.add(cb.equal(root.get("user").get("id"), getUserId()));
                }
                if(getProductId() != null){
                    predicates.add(cb.equal(root.get("product").get("id"), getProductId()));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }

                if(getStar() != null){
                    predicates.add(cb.equal(root.get("star"), getStar()));
                }

                if (!StringUtils.isEmpty(getMessage())) {
                    predicates.add(cb.like(cb.lower(root.get("message")), "%" + getMessage().toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
