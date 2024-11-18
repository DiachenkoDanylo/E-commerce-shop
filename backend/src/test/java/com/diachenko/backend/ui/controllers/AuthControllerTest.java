package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.services.UserAuthServiceImpl;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.dtos.auth.CredentialsDto;
import com.diachenko.backend.dtos.auth.SignUpDto;
import com.diachenko.backend.infrastructure.config.UserAuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private final UserDto userDto = new UserDto(1L, "client1", "client1", "client1", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJjbGllbnQxIiwibGFzdE5hbWUiOiJjbGllbnQxIiwiYXV0aG9yaXR5IjoiQ0xJRU5UIiwiaXNzIjoiY2xpZW50MSIsImV4cCI6MTczMTkzMzc5NywiaWF0IjoxNzMxOTMwMTk3fQ.eFt8DmpzCLaY3-4cooLRJDC1W0Lnkpp8tNyFCv7USo0", "client1@example.com", "CLIENT");
    private final String token = "token";
    char[] pass = {'p', 'a', 's', 's', '1'};
    private final CredentialsDto credentialsDto = new CredentialsDto("client1", pass);
    @MockBean
    private UserAuthServiceImpl userAuthService;
    @MockBean
    private UserAuthProvider userAuthProvider;
    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    @Test
//    void testLogin() throws Exception {
//        when(userAuthService.login(any())).thenReturn(userDto);
//        when(userAuthProvider.createToken(any())).thenReturn(token);
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(credentialsDto))
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.token").value("token"));
//    }


}
