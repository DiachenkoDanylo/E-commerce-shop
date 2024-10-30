package com.diachenko.backend.core.entities;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import org.springframework.data.jpa.domain.Specification;

public class ItemSpecifications {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CATEGORY = "category";
    private static final String PRICE = "basePrice";
    private static final String QUANTITY = "quantity";

    private ItemSpecifications() {
    }

    public static Specification<Item> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return all if no keyword
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get(NAME), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get(DESCRIPTION), "%" + keyword + "%")
            );
        };
    }

    public static Specification<Item> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(CATEGORY).get("id"), categoryId);
        };
    }

    public static Specification<Item> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.conjunction();
            } else if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get(PRICE), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), maxPrice);
            }
        };
    }

    public static Specification<Item> isInStock(Boolean inStock) {
        return (root, query, criteriaBuilder) -> {
            if (inStock == null) {
                return criteriaBuilder.conjunction(); // Return all if no stock filter
            }
            return inStock ? criteriaBuilder.greaterThan(root.get(QUANTITY), 0) : criteriaBuilder.equal(root.get(QUANTITY), 0);
        };
    }

}