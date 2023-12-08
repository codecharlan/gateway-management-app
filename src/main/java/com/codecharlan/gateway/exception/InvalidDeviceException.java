package com.codecharlan.gateway.exception;

import com.codecharlan.gateway.entity.PeripheralDevice;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class InvalidDeviceException extends RuntimeException {

    public InvalidDeviceException(Set<ConstraintViolation<PeripheralDevice>> violations) {
        super("Invalid device data: " + violations);
    }
}
