package com.codecharlan.gateway.service.serviceimpl;

import com.codecharlan.gateway.entity.Gateway;
import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.exception.DeviceNotFoundException;

public interface DeviceService {
    PeripheralDevice createDevice(PeripheralDevice peripheralDevice);

    void deleteDevice(Gateway gateway, Long id) throws DeviceNotFoundException;
}
