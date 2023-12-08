package com.codecharlan.gateway.service.serviceimpl;

import com.codecharlan.gateway.entity.PeripheralDevice;

public interface ConnectivityChecker {
    boolean performConnectivityCheck(PeripheralDevice device);
}