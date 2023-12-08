package com.codecharlan.gateway.dto.response;

public record ApiResponse<T>(String message, T data, int status) {
}
