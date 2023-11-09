package com.techmarket.api.model.criteria;

import com.techmarket.api.model.Product;
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
public class ProductCriteria {

    private Long id;
    private String name;
    private Integer status;
    private Double price;
    private Double saleOff;
    private Integer soldAmount;
    private Integer totalInStock;
    private Long brandId;
    private Long categoryId;

    public Specification<Product> getSpecification() {
        return new Specification<Product>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                if(getSaleOff()!=null)
                {
                    predicates.add(cb.equal(root.get("saleOff"),getSaleOff()));
                }
                if(getSoldAmount()!=null)
                {
                    predicates.add(cb.equal(root.get("soldAmount"),getSoldAmount()));
                }
                if(getCategoryId()!=null)
                {
                    predicates.add(cb.equal(root.get("category").get("id"),getCategoryId()));
                }
                if(getBrandId()!=null)
                {
                    predicates.add(cb.equal(root.get("brand").get("id"),getCategoryId()));
                }
                if(getTotalInStock()!=null)
                {
                    predicates.add(cb.equal(root.get("totalInStock"),getTotalInStock()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
