package com.diachenko.backend.dtos;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;
//    private String refreshToken;
    private String email;
    private String authority;

}
