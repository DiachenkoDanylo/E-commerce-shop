package com.diachenko.backend.infrastructure.repositories;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String login);
}
