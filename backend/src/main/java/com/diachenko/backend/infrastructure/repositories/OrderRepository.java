package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce-shop
    26.09.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrderByUserId(Long clientUserId);
    List<Order> findAllByStatus(OrderStatus status);
    Optional<Order> findOrderByUserIdAndStatus( Long userId,OrderStatus status);

}
