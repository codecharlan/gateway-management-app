package com.codecharlan.gateway.exception;

public class MaximumDevicesReachedException extends RuntimeException {
    public MaximumDevicesReachedException(String serialNumber) {
        super("Gateway with serial number " + serialNumber + " has reached the maximum number of devices");
    }
}
