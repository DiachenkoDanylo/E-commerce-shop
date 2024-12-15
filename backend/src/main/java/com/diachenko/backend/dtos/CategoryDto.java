package com.diachenko.backend.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

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
public class CategoryDto implements Serializable {

    @Nullable
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;
}
