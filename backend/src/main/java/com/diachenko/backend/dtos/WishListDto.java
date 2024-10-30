package com.diachenko.backend.dtos;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class WishListDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long itemId;
}
