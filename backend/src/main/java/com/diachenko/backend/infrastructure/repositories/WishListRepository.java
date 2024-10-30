package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUserId(Long userId);

    Optional<WishList> findByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserIdAndItemId(Long userId, Long itemId);
}
