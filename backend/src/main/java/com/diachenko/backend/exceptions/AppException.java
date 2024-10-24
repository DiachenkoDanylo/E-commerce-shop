package com.diachenko.backend.exceptions;
/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
