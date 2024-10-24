package com.diachenko.backend.dtos.auth;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/


public record SignUpDto(String firstName, String lastName,
                        String login, String email, char[] password) {
}
