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
    private String brandName;
    private String categoryName;
    private String avgStart;


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
                if (!StringUtils.isBlank(getCategoryName()))
                {
                    Join<Product, Category> joinCategory = root.join("category",JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinCategory.get("name")), "%"+ getCategoryName().toLowerCase() +"%"));
                }
                if (!StringUtils.isBlank(getBrandName()))
                {
                    Join<Product, Brand> joinBrand = root.join("brand",JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinBrand.get("name")), "%"+ getBrandName().toLowerCase() +"%"));
                }
                if(getAvgStart()!=null)
                {
                    predicates.add(cb.equal(root.get("avgStart"),getAvgStart()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
