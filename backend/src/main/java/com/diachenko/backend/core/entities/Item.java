package com.diachenko.backend.core.entities;
/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Setter
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer basePrice;

    @Column
    private Integer quantity;

}
