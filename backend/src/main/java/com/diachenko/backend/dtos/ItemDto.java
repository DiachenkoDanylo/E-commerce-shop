package com.diachenko.backend.dtos;
/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class ItemDto {

    @Nullable
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long categoryId;

    @NotNull
    private String description;

    @NotNull
    private Integer basePrice;

    @Nullable
    private List<ImageDto> images;

    @NotNull
    private Integer quantity;

}
