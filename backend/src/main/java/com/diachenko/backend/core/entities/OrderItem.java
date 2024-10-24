package com.diachenko.backend.core.entities;
/*  E-commerce-shop
    08.10.2024
    @author DiachenkoDanylo
*/


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "order_items")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price")
    private Integer totalPrice;
}
