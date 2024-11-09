package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.UserAuthServiceImpl;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.dtos.auth.CredentialsDto;
import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.infrastructure.config.UserAuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    private UserAuthServiceImpl userAuthService;
    @MockBean
    private UserAuthProvider userAuthProvider;
    @Autowired
    private MockMvc mockMvc;

    private final UserDto userDto = new UserDto(1L, "testname", "testlname", "testlog", "token", "testmail", "ADMIN");

    char[] pass = {'p', 'a', 's', 's', '1'};
    private final CredentialsDto credentialsDto = new CredentialsDto("testlog",pass);
    private final String token = "token";

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testLogin() throws Exception {
        when(userAuthService.login(credentialsDto)).thenReturn(userDto);
        when(userAuthProvider.createToken(userDto)).thenReturn(token);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(credentialsDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userAuthService.login(credentialsDto);
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
            UserDto userDto = userAuthService.registerAdmin(signUpDto);
            userDto.setToken(userAuthProvider.createToken(userDto));
            userDTO = userDto;
        } else {
            UserDto user = userAuthService.register(signUpDto);
            user.setToken(userAuthProvider.createToken(user));
            userDTO = user;
        }
        return ResponseEntity.created(URI.create("/users/" + userDTO.getId())).body(userDTO);
    }


}
