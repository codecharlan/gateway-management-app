package com.codecharlan.gateway.exception;

public class GatewayNotFoundException extends RuntimeException {
    public GatewayNotFoundException(String serialNumber) {
        super("Gateway with serial number " + serialNumber + " not found");
    }
}
