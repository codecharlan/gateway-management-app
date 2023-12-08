package com.codecharlan.gateway.service;

import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.entity.Gateway;
import com.codecharlan.gateway.exception.DeviceNotFoundException;
import com.codecharlan.gateway.service.serviceimpl.DeviceService;
import lombok.RequiredArgsConstructor;
import com.codecharlan.gateway.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    @Override
    public PeripheralDevice createDevice(PeripheralDevice peripheralDevice) {
        return deviceRepository.save(peripheralDevice);
    }
    @Override
    public void deleteDevice(Gateway gateway, Long id) throws DeviceNotFoundException {
        PeripheralDevice peripheralDevice = deviceRepository.findByIdAndGateway(id, gateway).orElseThrow(() -> new DeviceNotFoundException(id));
        deviceRepository.delete(peripheralDevice);
    }
}
