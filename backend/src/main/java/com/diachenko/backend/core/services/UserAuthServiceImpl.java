package com.diachenko.backend.core.services;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.UserAuthService;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.dtos.auth.CredentialsDto;
import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.UserMapper;
import com.diachenko.backend.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown User", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        } else {
            throw new AppException("Invalid Password", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserDto register(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByLogin(signUpDto.login());

        if (user.isPresent()) {
            throw new AppException("The user with login: " + signUpDto.login() + " are already exist", HttpStatus.BAD_REQUEST);
        } else {
            User userEntity = userMapper.signUpToUser(signUpDto);
            userEntity.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
            userEntity.setAuthority("CLIENT");
            User savedUser = userRepository.save(userEntity);
            return userMapper.toUserDto(savedUser);
        }
    }

    @Override
    public UserDto registerAdmin(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByLogin(signUpDto.login());

        if (user.isPresent()) {
            throw new AppException("The user with login: " + signUpDto.login() + " are already exist", HttpStatus.BAD_REQUEST);
        } else {
            User userEntity = userMapper.signUpToUser(signUpDto);
            userEntity.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
            userEntity.setAuthority("ADMIN");
            User savedUser = userRepository.save(userEntity);
            return userMapper.toUserDto(savedUser);
        }
    }
}
