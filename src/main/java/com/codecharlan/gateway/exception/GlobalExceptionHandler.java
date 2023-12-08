package com.codecharlan.gateway.exception;

import com.codecharlan.gateway.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", errors, NOT_ACCEPTABLE.value()), NOT_ACCEPTABLE);
    }
    @ExceptionHandler(GatewayNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleGatewayNotFoundException(GatewayNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), NOT_FOUND.value()), BAD_REQUEST);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleDeviceNotFoundException(DeviceNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), NOT_FOUND.value()), NOT_FOUND);
    }
    @ExceptionHandler(InvalidDeviceException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidDeviceException(InvalidDeviceException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(MaximumDevicesReachedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleMaximumDevicesReachedException(MaximumDevicesReachedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
    @ExceptionHandler(InvalidGatewayException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidGatewayException(InvalidGatewayException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
}
