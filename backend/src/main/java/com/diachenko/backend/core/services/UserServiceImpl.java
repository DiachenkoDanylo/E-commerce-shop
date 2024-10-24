package com.diachenko.backend.core.services;

import com.diachenko.backend.application.services.UserService;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;

    @Override
    public User getUserByLoginAuth(Authentication authentication) {
        String login = jwtServiceImpl.getUserLoginFromToken(authentication);
        return userRepository.findByLogin(login).
                orElseThrow(() -> new AppException("User not Found", HttpStatus.NOT_FOUND));
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).
                orElseThrow(() -> new AppException("User not Found", HttpStatus.NOT_FOUND));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new AppException("User not Found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
