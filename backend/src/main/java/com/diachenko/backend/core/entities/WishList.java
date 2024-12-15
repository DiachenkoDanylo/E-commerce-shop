package com.diachenko.backend.core.entities;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Setter
@Getter
public class WishList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "added_date", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}