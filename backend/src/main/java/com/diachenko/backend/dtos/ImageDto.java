package com.diachenko.backend.dtos;
/*  E-commerce-shop
    31.10.2024
    @author DiachenkoDanylo
*/

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class ImageDto  implements Serializable {

    @Nullable
    private Long id;

    @NotNull
    private String imageUrl;
}
