package com.diachenko.backend.dtos;
/*  E-commerce-shop
    31.10.2024
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
public class ImageDto {

    @Nullable
    private Long id;

    @NotNull
    private String imageUrl;
}
