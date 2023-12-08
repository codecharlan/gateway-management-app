package com.codecharlan.gateway.service;

import com.codecharlan.gateway.dto.request.GatewayRequest;
import com.codecharlan.gateway.dto.response.ApiResponse;
import com.codecharlan.gateway.dto.response.GatewayResponse;
import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.entity.Gateway;
import com.codecharlan.gateway.exception.DeviceNotFoundException;
import com.codecharlan.gateway.exception.GatewayNotFoundException;
import com.codecharlan.gateway.exception.InvalidDeviceException;
import com.codecharlan.gateway.exception.InvalidGatewayException;
import com.codecharlan.gateway.exception.MaximumDevicesReachedException;
import com.codecharlan.gateway.repository.GatewayRepository;
import com.codecharlan.gateway.service.serviceimpl.DeviceService;
import com.codecharlan.gateway.service.serviceimpl.GatewayService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@Service
public class GatewayServiceImpl implements GatewayService {

    private final GatewayRepository gatewayRepository;
    private final DeviceService deviceService;
    private final Validator validator;

    @Override
    public ApiResponse<GatewayResponse> saveGateway(GatewayRequest gatewayRequest) throws InvalidGatewayException {
        Gateway gateway = Gateway.builder()
                .serialNumber(gatewayRequest.getSerialNumber())
                .name(gatewayRequest.getName())
                .ipAddress(gatewayRequest.getIpAddress())
                .createdAt(LocalDateTime.now())
                .build();

        validateGateway(gateway);

        Gateway savedGateway = gatewayRepository.save(gateway);

        GatewayResponse gatewayResponse = fromGateway(savedGateway);

        return new ApiResponse<>("Gateway saved successfully", gatewayResponse, CREATED.value());
    }

    @Override
    public ApiResponse<List<GatewayResponse>> getAllGateways() {
        List<Gateway> gateways = gatewayRepository.findAll();
        List<GatewayResponse> responses = new ArrayList<>();
        for (Gateway gateway : gateways) {
            responses.add(GatewayResponse.builder()
                    .serialNumber(gateway.getSerialNumber())
                    .name(gateway.getName())
                    .ipAddress(gateway.getIpAddress())
                    .createdAt(gateway.getCreatedAt())
                    .peripheralDevices(gateway.getPeripheralDevices())
                    .build());
        }
        return new ApiResponse<>("Gateways retrieved successfully", responses, OK.value());
    }

    @Override
    public ApiResponse<Gateway> getGatewayBySerialNumber(String serialNumber) throws GatewayNotFoundException {
        Gateway gateway = gatewayRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new GatewayNotFoundException(serialNumber));
        return new ApiResponse<>("Gateway retrieved successfully", gateway, OK.value());
    }


    @Override
    public ApiResponse<GatewayResponse> addPeripheralDevice(String serialNumber, PeripheralDevice peripheralDevice) throws GatewayNotFoundException, InvalidDeviceException, MaximumDevicesReachedException {
        ApiResponse<Gateway> gateway = getGatewayBySerialNumber(serialNumber);
        validateDevice(peripheralDevice);
        Gateway gatewayResponse = gateway.data();
        if (gatewayResponse.getPeripheralDevices().size() >= 10) {
            throw new MaximumDevicesReachedException(gatewayResponse.getSerialNumber());
        }

       PeripheralDevice device = deviceService.createDevice(PeripheralDevice.builder()
                .gateway(gatewayResponse)
                .uid(peripheralDevice.getUid())
                .vendor(peripheralDevice.getVendor())
                .createdAt(LocalDateTime.now())
                .status(true)
                .build());
       List<PeripheralDevice> devices = new ArrayList<>();
       devices.add(device);
       gatewayResponse.setPeripheralDevices(devices);

        GatewayResponse newGatewayResponse = fromGateway(gatewayRepository.save(gatewayResponse));
        return new ApiResponse<>("Peripheral device added successfully", newGatewayResponse, OK.value());
    }

    public static GatewayResponse fromGateway(Gateway gateway) {
        return GatewayResponse.builder()
                .serialNumber(gateway.getSerialNumber())
                .name(gateway.getName())
                .ipAddress(gateway.getIpAddress())
                .createdAt(gateway.getCreatedAt())
                .peripheralDevices(gateway.getPeripheralDevices())
                .build();
    }

    @Override
    public ApiResponse<?> removePeripheralDevice(String serialNumber, Long id) throws GatewayNotFoundException, DeviceNotFoundException {
        ApiResponse<Gateway> gateway = getGatewayBySerialNumber(serialNumber);
        Gateway gatewayResponse = gateway.data();
        deviceService.deleteDevice(gatewayResponse, id);
        return new ApiResponse<>("Peripheral device removed successfully", null, OK.value());
    }

    private void validateGateway(Gateway gateway) throws InvalidGatewayException {
        Set<ConstraintViolation<Gateway>> violations = validator.validate(gateway);
        if (!violations.isEmpty()) {
            throw new InvalidGatewayException(violations);
        }
    }

    private void validateDevice(PeripheralDevice peripheralDevice) throws InvalidDeviceException {
        Set<ConstraintViolation<PeripheralDevice>> violations = validator.validate(peripheralDevice);
        if (!violations.isEmpty()) {
            throw new InvalidDeviceException(violations);
        }
    }
}
