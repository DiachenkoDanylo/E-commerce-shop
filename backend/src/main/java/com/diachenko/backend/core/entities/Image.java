package com.diachenko.backend.core.entities;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Setter
@Getter
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonBackReference
    private Item item;

}
