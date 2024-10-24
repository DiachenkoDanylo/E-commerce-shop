package com.diachenko.backend.application.services;

import com.diachenko.backend.core.entities.User;
import org.springframework.security.core.Authentication;

import java.util.List;

/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/

public interface UserService {

    User getUserByLoginAuth(Authentication authentication);

    User getUserByLogin(String login);

    User getUserById(Long id);

    List<User> getAllUsers();
}
