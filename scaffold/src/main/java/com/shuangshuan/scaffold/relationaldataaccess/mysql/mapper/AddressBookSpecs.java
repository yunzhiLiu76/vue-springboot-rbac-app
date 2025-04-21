package com.shuangshuan.scaffold.relationaldataaccess.mysql.mapper;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.AddressBook;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

//以后业务复杂了把Specification抽取出来复用
public class AddressBookSpecs {

    public static Specification<AddressBook> findByUserId(Long id) {
        return (root, query, builder) -> {
            return builder.equal(root.get("userId"), id);
        };
    }

    public static Specification<AddressBook> fuzzyNameSearch(String keyword) {
        return (root, query, builder) -> {
            // build query here
            return builder.like(root.get("consignee"),"%" + keyword + "%");
        };
    }

    public static Specification<AddressBook> findIdBetween(String keyword) {
        return (root, query, builder) -> {
            // build query here
            return builder.between(root.get("id"), 2, 5);
        };
    }

    public static Specification<AddressBook> combinedSpecification(Long id, String consignee) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (Objects.nonNull(id)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userId"), id));
            }
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("consignee"), "%" + consignee + "%"));
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("id"), 2, 5));
            return predicate;
        };
    }

}
