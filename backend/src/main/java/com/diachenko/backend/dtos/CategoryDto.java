package com.diachenko.backend.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class CategoryDto {

    @Nullable
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;
}
