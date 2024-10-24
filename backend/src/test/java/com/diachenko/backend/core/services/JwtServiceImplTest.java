package com.diachenko.backend.core.services;
/*  E-commerce-shop
    16.10.2024
    @author DiachenkoDanylo
*/

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.diachenko.backend.core.entities.Authority;
import com.diachenko.backend.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceImplTest {

    private final UserDto user = new UserDto(1L, "testname", "testlname", "testlog", "testpass", "testmail", "TEST_AUTHORITY");

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public Authentication auth() {
        return validateToken(createToken(user));

    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secretKey");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        UserDto user2 = UserDto.builder()
                .login(decodedJWT.getIssuer())
                .firstName(decodedJWT.getClaim("firstName").asString())
                .lastName(decodedJWT.getClaim("lastName").asString())
                .authority(decodedJWT.getClaim("authority").asString())
                .build();
        return new UsernamePasswordAuthenticationToken(user2, null, List.of(new Authority(user.getAuthority())));
    }

    public String createToken(UserDto userDto) {
        Date now = new Date();
        Date validaty = new Date(now.getTime() + 3_600_000);
        return JWT.create()
                .withIssuer(userDto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validaty)
                .withClaim("firstName", userDto.getFirstName())
                .withClaim("lastName", userDto.getLastName())
                .withClaim("authority", userDto.getAuthority())
                .sign(Algorithm.HMAC256("secretKey"));
    }

    @Test
    void testAuthToPrincipalString() {
        String authority = jwtService.authToPrincipalString(auth());
        int a = authority.indexOf("authority=");
        String substring = authority.substring(a + 10, authority.length() - 1);
        assertEquals(user.getAuthority(), substring);
    }

    @Test
    void testGetUserLoginFromToken() {
        assertEquals(user.getLogin(), jwtService.getUserLoginFromToken(auth()));
    }

    @Test
    void testGetUserFirstnameFromToken() {
        assertEquals(user.getFirstName(), jwtService.getUserFirstnameFromToken(auth()));
    }

    @Test
    void testGetUserLastnameFromToken() {
        assertEquals(user.getLastName(), jwtService.getUserLastnameFromToken(auth()));
    }
}
