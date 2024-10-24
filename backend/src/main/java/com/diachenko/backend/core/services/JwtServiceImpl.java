package com.diachenko.backend.core.services;
/*  E-commerce-shop
    04.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {


    @Override
    public String authToPrincipalString(Authentication auth) {
        return auth.getPrincipal().toString();
    }

    @Override
    public String getUserLoginFromToken(Authentication auth) {
        String token = authToPrincipalString(auth);
        return token.substring(token.indexOf("login") + 6, token.indexOf("token") - 2);
    }

    @Override
    public String getUserFirstnameFromToken(Authentication auth) {
        String token = authToPrincipalString(auth);
        return token.substring(token.indexOf("firstName") + 10, token.indexOf("lastName") - 2);
    }

    @Override
    public String getUserLastnameFromToken(Authentication auth) {
        String token = authToPrincipalString(auth);
        return token.substring(token.indexOf("lastName") + 9, token.indexOf("login") - 2);
    }


}
