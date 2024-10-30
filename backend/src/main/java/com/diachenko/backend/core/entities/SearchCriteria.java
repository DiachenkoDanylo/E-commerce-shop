package com.diachenko.backend.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SearchCriteria {

    private String keyword;
    private Long categoryId;
    private Double minPrice;
    private Double maxPrice;
    private Boolean inStock;
}
