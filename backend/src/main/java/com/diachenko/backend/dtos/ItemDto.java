package com.diachenko.backend.dtos;
/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class ItemDto  {

    @Nullable
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer basePrice;

    @NotNull
    private Integer quantity;

}
