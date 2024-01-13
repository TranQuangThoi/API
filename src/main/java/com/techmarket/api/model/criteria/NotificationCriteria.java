package com.techmarket.api.model.criteria;

import com.techmarket.api.model.Notification;
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
public class NotificationCriteria {
    private Long id;
    private Long idUser;
    private Integer kind;
    private Integer status;
    private String msg;


    public Specification<Notification> getCriteria() {
        return new Specification<Notification>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Notification> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getIdUser() != null){
                    predicates.add(cb.equal(root.get("idUser"), getIdUser()));
                }
                if(getKind() != null){
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(!StringUtils.isBlank(getMsg())){
                    predicates.add(cb.like(cb.lower(root.get("msg")),"%"+ getMsg()+"%"));
                }
                query.orderBy(cb.desc(root.get("createdDate")));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
