package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    17.10.2024
    @author DiachenkoDanylo
*/

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.diachenko.backend.core.entities.Authority;
import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.OrderServiceImpl;
import com.diachenko.backend.core.services.OrderStatusServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.exceptions.AppException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    private final UserDto user = new UserDto(1L, "testname", "testlname", "testlog", "testpass", "testmail", "TEST_AUTHORITY");
    User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "ADMIN");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @MockBean
    private OrderStatusServiceImpl orderStatusServiceImpl;

    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, SIZE);
    private static final String BASE_URI = "/order/";

    @BeforeEach
    void setUp() {
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
    @WithMockUser(username = "testuser", authorities = {"CLIENT"})
    void testGetOrderById() throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setStatus(OrderStatus.CART);

        when(userServiceImpl.getUserByLoginAuth(any(Authentication.class))).thenReturn(userTest);
        when(orderService.getCartOrderDtoByUserId(userTest.getId())).thenReturn(orderDto);

        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("CART"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"CLIENT"})
    void testGetAllOrders() throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setStatus(OrderStatus.CART);
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setId(2L);
        orderDto2.setStatus(OrderStatus.CREATED);

        when(userServiceImpl.getUserByLoginAuth(any(Authentication.class))).thenReturn(userTest);
        when(orderService.getAllOrdersDtoByUserId(userTest.getId(), PAGE, SIZE)).thenReturn(new PageImpl<>(List.of(orderDto, orderDto2), PAGEABLE, 2));

        mockMvc.perform(get(BASE_URI+"all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].status").value("CART"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].status").value("CREATED"));
    }
//
//    @GetMapping
//    public ResponseEntity<OrderDto> getCurrentOrder(Authentication auth) {
//        User user = userServiceImpl.getUserByLoginAuth(auth);
//        return ResponseEntity.ok(orderServiceImpl.getCartOrderDtoByUserId(user.getId()));
//    }
//
//    @GetMapping("all")
//    public ResponseEntity<Page<OrderDto>> getAllOrders(Authentication auth,
//                                                       @RequestParam(name = "page", defaultValue = "0") int page,
//                                                       @RequestParam(name = "size", defaultValue = "10") int size) {
//        User user = userServiceImpl.getUserByLoginAuth(auth);
//        return ResponseEntity.ok(orderServiceImpl.getAllOrdersDtoByUserId(user.getId(),page,size));
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<OrderDto> getOrderClient(Authentication auth, @PathVariable("id") Long id) {
//        if (orderServiceImpl.getOrderDtoById(id).getUser().getLogin().equals(userServiceImpl.getUserByLoginAuth(auth).getLogin()) || auth.getPrincipal().toString().contains("ADMIN")) {
//            return ResponseEntity.ok(orderServiceImpl.getOrderDtoById(id));
//        } else {
//            throw new AppException("You cannot get others client orders", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PutMapping("{id}")
//    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDto orderDto) {
//        return ResponseEntity.ok(orderServiceImpl.updateOrder(id, orderDto));
//    }

}
