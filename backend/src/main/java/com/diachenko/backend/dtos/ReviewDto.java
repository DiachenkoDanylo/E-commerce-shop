package com.diachenko.backend.dtos;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDto {

    @Nullable
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    private int rating;

    @Nullable
    private String comment;

    @Nullable
    private LocalDateTime createdAt;

    @NotNull
    private Long orderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewDto reviewDto = (ReviewDto) o;
        return rating == reviewDto.rating && Objects.equals(itemId, reviewDto.itemId) && Objects.equals(orderId, reviewDto.orderId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(itemId);
        result = 31 * result + rating;
        result = 31 * result + Objects.hashCode(comment);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(orderId);
        return result;
    }
}
