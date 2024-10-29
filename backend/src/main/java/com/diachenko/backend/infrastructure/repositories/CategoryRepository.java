package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
