package com.techmarket.api.model.criteria;

import com.techmarket.api.model.ProductVariant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductVariantCriteria {

    private Long id;
    private String name;
    private Integer status;
    private Double price;
    private Integer totalInStock;
    private Long productId;
    private String color;

    public Specification<ProductVariant> getSpecification() {
        return new Specification<ProductVariant>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProductVariant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId()!=null)
                {
                    predicates.add(cb.equal(root.get("id"),getId()));
                }
                if(getStatus()!=null)
                {
                    predicates.add(cb.equal(root.get("status"),getStatus()));
                }
                if (!StringUtils.isBlank(getName()))
                {
                    predicates.add(cb.like(cb.lower(root.get("name")),"%"+ getName()+"%"));
                }
                if(getPrice()!=null)
                {
                    predicates.add(cb.equal(root.get("price"),getPrice()));
                }
                if (getTotalInStock() != null) {
                    predicates.add(cb.greaterThan(root.get("totalStock"), 0));
                }
                if(getProductId()!=null)
                {
                    predicates.add(cb.equal(root.get("product").get("id"),getProductId()));
                }
                if(!StringUtils.isBlank(getColor()))
                {
                    predicates.add(cb.equal(root.get("color"),getColor()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
