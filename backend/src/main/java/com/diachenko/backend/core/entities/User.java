package com.diachenko.backend.core.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/*  E-commerce-shop
    02.10.2024
    @author DiachenkoDanylo
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "app_user")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private String authority;

}
