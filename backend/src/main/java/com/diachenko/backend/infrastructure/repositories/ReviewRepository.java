package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByItem_Id(Long itemId);

}
