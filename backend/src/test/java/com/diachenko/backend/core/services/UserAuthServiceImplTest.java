package com.diachenko.backend.core.services;
/*  E-commerce-shop
    16.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.dtos.auth.CredentialsDto;
import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.UserMapper;
import com.diachenko.backend.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String codedPass = "$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i";
    private final User user = new User(1L, "testname", "testlname", "testlog", codedPass, "testmail", "TEST_AUTHORITY");
    private final UserDto userDto = new UserDto(1L, "testname", "testlname", "testlog", "pass1", "testmail", "TEST_AUTHORITY");
    char[] pass = {'p', 'a', 's', 's', '1'};

    private final CredentialsDto credentialsDto = new CredentialsDto("testlog", pass);

    @Test
    void testLogin() {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),codedPass)).thenReturn(true);
        assertEquals(userAuthService.login(credentialsDto), userDto);

    }

    @Test
    void testLogin_AppException_UnknownUser() {
        when(userRepository.findByLogin(user.getLogin())).thenThrow( new AppException("Unknown User", HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> userAuthService.login(credentialsDto));

        assertEquals("Unknown User", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

        verify(userMapper,times(0)).toUserDto(user);
    }

    @Test
    void testLogin_AppException_InvalidPassword() {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),codedPass)).thenReturn(false);
        AppException thrown = assertThrows(AppException.class, () -> userAuthService.login(credentialsDto));

        assertEquals("Invalid Password", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());

        verify(userMapper,times(0)).toUserDto(user);
    }

    @Test
    void testRegister() {
        SignUpDto signUpDto = new SignUpDto("testname", "testlastname","testlogin", "testemail", pass);

        User userUpdated = new User(1L, "testname", "testlname", "testlog", codedPass, "testmail", "CLIENT");
        UserDto userDtoUpdated = new UserDto(1L, "testname", "testlname", "testlog", "pass1", "testmail", "CLIENT");

        when(userRepository.findByLogin(signUpDto.login())).thenReturn(Optional.empty());

        when(userMapper.signUpToUser(signUpDto)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(userUpdated);

        when(userMapper.toUserDto(userUpdated)).thenReturn(userDtoUpdated);

        assertEquals(userDtoUpdated,userAuthService.register(signUpDto));
    }

    @Test
    void testRegister_AppException() {
        SignUpDto signUpDto = new SignUpDto("testname", "testlastname","testlogin", "testemail", pass);

        when(userRepository.findByLogin(signUpDto.login())).thenReturn(Optional.of(user));

        AppException thrown = assertThrows(AppException.class, () -> userAuthService.register(signUpDto));

        assertEquals("The user with login: " + signUpDto.login() + " are already exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        verify(userMapper,times(0)).signUpToUser(any());
    }

    @Test
    void testRegisterAdmin() {
        SignUpDto signUpDto = new SignUpDto("testname", "testlastname","testlogin", "testemail", pass);

        User userUpdated = new User(1L, "testname", "testlname", "testlog", codedPass, "testmail", "CLIENT");
        UserDto userDtoUpdated = new UserDto(1L, "testname", "testlname", "testlog", "pass1", "testmail", "ADMIN");

        when(userRepository.findByLogin(signUpDto.login())).thenReturn(Optional.empty());

        when(userMapper.signUpToUser(signUpDto)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(userUpdated);

        when(userMapper.toUserDto(userUpdated)).thenReturn(userDtoUpdated);

        assertEquals(userDtoUpdated,userAuthService.register(signUpDto));
    }

    @Test
    void testRegisterAdmin_AppException() {
        SignUpDto signUpDto = new SignUpDto("testname", "testlastname","testlogin", "testemail", pass);

        when(userRepository.findByLogin(signUpDto.login())).thenReturn(Optional.of(user));

        AppException thrown = assertThrows(AppException.class, () -> userAuthService.register(signUpDto));

        assertEquals("The user with login: " + signUpDto.login() + " are already exist", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        verify(userMapper,times(0)).signUpToUser(any());
    }
}

