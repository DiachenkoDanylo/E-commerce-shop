package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
