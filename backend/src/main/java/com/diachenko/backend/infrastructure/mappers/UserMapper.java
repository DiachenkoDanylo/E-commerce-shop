package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    12.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.core.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {User.class})
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(UserDto user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
