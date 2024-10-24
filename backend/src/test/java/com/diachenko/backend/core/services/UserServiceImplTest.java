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
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtServiceImpl jwtServiceImpl;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final UserDto userDto = new UserDto(1L, "testname", "testlname", "testlog", "testpass", "testmail", "TEST_AUTHORITY");
    private final User user = new User(1L, "testname", "testlname", "testlog", "testpass", "testmail", "TEST_AUTHORITY");

    public Authentication auth() {
        return validateToken(createToken(userDto));

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
        return new UsernamePasswordAuthenticationToken(user2, null, List.of(new Authority(userDto.getAuthority())));
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
    void testGetUserByLoginAuth() {
        when(jwtServiceImpl.getUserLoginFromToken(auth())).thenReturn(userDto.getLogin());
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));

        assertEquals(userService.getUserByLoginAuth(auth()), user);
        verify(userRepository,times(1)).findByLogin(userDto.getLogin());
    }

    @Test
    void testGetUserByLoginAuth_AppException() {
        when(jwtServiceImpl.getUserLoginFromToken(auth())).thenReturn(userDto.getLogin());
        when(userRepository.findByLogin(userDto.getLogin())).thenThrow(new AppException("User not found",HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> userRepository.findByLogin(userDto.getLogin()));

        assertEquals("User not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

        verify(userRepository,times(1)).findByLogin(userDto.getLogin());
    }

    @Test
    void testGetUserByLogin() {
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));

        assertEquals(userService.getUserByLogin(userDto.getLogin()), user);
    }

    @Test
    void testGetUserByLogin_AppException() {
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> userService.getUserByLogin(user.getLogin()));

        assertEquals("User not Found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        verify(userRepository,times(1)).findByLogin(userDto.getLogin());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getUserById(1L));
    }

    @Test
    void testGetUserById_AppException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> userService.getUserById(user.getId()));

        assertEquals("User not Found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        verify(userRepository,times(1)).findById(userDto.getId());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertEquals(List.of(user), userService.getAllUsers());
    }
}
