package com.diachenko.backend.core.entities;
/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column
    private String description;

    @Column
    private Integer basePrice;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @Column
    private Integer quantity;

    public void addImageToItem(Image image) {
        if (this.images != null) {
            this.images.add(image);
        } else {
            this.images = new ArrayList<>();
            this.images.add(image);
        }
    }
}
