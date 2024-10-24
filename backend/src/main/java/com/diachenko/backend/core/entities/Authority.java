package com.diachenko.backend.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/*  E-commerce-shop
    03.10.2024
    @author DiachenkoDanylo
*/
@Table(name = "authority")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Setter
@Getter
public class Authority implements GrantedAuthority {


    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
