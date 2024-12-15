package com.techmarket.api.model.criteria;

import com.techmarket.api.model.Brand;
import com.techmarket.api.model.Category;
import com.techmarket.api.model.Product;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class BrandCriteria {
    private Long id;
    private String name;
    private Integer status;

    private String categoryName;

    public Specification<Brand> getSpecification() {
        return new Specification<Brand>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                if (getCategoryName() != null) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Product> productRoot = subquery.from(Product.class);
                    Join<Product, Category> categoryJoin = productRoot.join("category");


                    subquery.select(productRoot.get("brand").get("id"))
                            .where(cb.like(cb.lower(categoryJoin.get("name")), "%" + getCategoryName().toLowerCase() + "%"));

                    predicates.add(cb.in(root.get("id")).value(subquery));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
