package com.diachenko.backend.application.services;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.dtos.auth.CredentialsDto;
import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.dtos.UserDto;


public interface UserAuthService {


    UserDto login(CredentialsDto credentialsDto);

    UserDto register(SignUpDto signUpDto);

    UserDto registerAdmin(SignUpDto signUpDto);
}
