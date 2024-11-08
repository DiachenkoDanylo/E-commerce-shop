package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByItemId(Long itemId);
    void deleteAllByItem_Id(Long itemId);
}
