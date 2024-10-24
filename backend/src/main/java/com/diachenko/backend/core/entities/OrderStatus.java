package com.diachenko.backend.core.entities;
/*  E-commerce-shop
    04.10.2024
    @author DiachenkoDanylo
*/

import lombok.Getter;

@Getter
public enum OrderStatus {
    CART(0),
    CREATED(1),
    PAID(2),
    IN_PROGRESS(3),
    POST_DELIVERY(4),
    COMPLETED(5),
    CANCELLED(6);

    private final int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + code);
    }
}
