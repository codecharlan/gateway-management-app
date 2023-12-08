package com.codecharlan.gateway.exception;

import com.codecharlan.gateway.entity.Gateway;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class InvalidGatewayException extends RuntimeException {
    public InvalidGatewayException(Set<ConstraintViolation<Gateway>> violations) {
        super("Invalid gateway data: " + violations);
    }
}
