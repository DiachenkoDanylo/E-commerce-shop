package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.services.UserAuthServiceImpl;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.dtos.auth.CredentialsDto;
import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.infrastructure.config.UserAuthProvider;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthControllerTest {

    private final UserAuthServiceImpl userAuthServiceImpl;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userAuthServiceImpl.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto,
                                            @RequestParam( name = "admin")
                                            @Nullable
                                             @DefaultValue(value = "false") String admin) {
        UserDto userDTO;
        if (admin != null && admin.equals("true")) {
            UserDto userDto = userAuthServiceImpl.registerAdmin(signUpDto);
            userDto.setToken(userAuthProvider.createToken(userDto));
            userDTO = userDto;
        } else {
            UserDto user = userAuthServiceImpl.register(signUpDto);
            user.setToken(userAuthProvider.createToken(user));
            userDTO = user;
        }
        return ResponseEntity.created(URI.create("/users/" + userDTO.getId())).body(userDTO);
    }


}
