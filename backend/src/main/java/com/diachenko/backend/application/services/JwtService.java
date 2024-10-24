package com.diachenko.backend.application.services;
/*  E-commerce-shop
    04.10.2024
    @author DiachenkoDanylo
*/

import org.springframework.security.core.Authentication;

public interface JwtService {

    String authToPrincipalString(Authentication auth);

    String getUserLoginFromToken(Authentication auth);

    String getUserFirstnameFromToken(Authentication auth);

    String getUserLastnameFromToken(Authentication auth);


}
