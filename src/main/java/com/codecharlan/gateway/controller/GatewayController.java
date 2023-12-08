package com.codecharlan.gateway.controller;

import com.codecharlan.gateway.dto.request.GatewayRequest;
import com.codecharlan.gateway.dto.response.ApiResponse;
import com.codecharlan.gateway.dto.response.GatewayResponse;
import com.codecharlan.gateway.entity.Gateway;
import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.exception.DeviceNotFoundException;
import com.codecharlan.gateway.exception.GatewayNotFoundException;
import com.codecharlan.gateway.exception.InvalidDeviceException;
import com.codecharlan.gateway.exception.InvalidGatewayException;
import com.codecharlan.gateway.exception.MaximumDevicesReachedException;
import com.codecharlan.gateway.service.serviceimpl.GatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.codecharlan.gateway.service.GatewayServiceImpl.fromGateway;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gateway")
@CrossOrigin("*")
public class GatewayController {

    private final GatewayService gatewayService;

    @PostMapping("/save")
    public ResponseEntity<GatewayResponse> createGateway(@RequestBody @Valid GatewayRequest gateway) throws InvalidGatewayException {
        ApiResponse<GatewayResponse> createdGateway = gatewayService.saveGateway(gateway);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGateway.data());
    }

    @GetMapping("/all")
    public ResponseEntity<List<GatewayResponse>> getAllGateways() {
        ApiResponse<List<GatewayResponse>> gateways = gatewayService.getAllGateways();
        return ResponseEntity.status(HttpStatus.OK).body(gateways.data());
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<GatewayResponse> getGatewayBySerialNumber(@PathVariable String serialNumber) throws GatewayNotFoundException {
        ApiResponse<Gateway> gateway = gatewayService.getGatewayBySerialNumber(serialNumber);
        return ResponseEntity.status(HttpStatus.OK).body(fromGateway(gateway.data()));
    }

    @PutMapping("/devices/add")
    public ResponseEntity<GatewayResponse> addDevice(@RequestParam String serialNumber, @RequestBody @Valid PeripheralDevice device) throws GatewayNotFoundException, InvalidDeviceException, MaximumDevicesReachedException {
        ApiResponse<GatewayResponse> updatedGateway = gatewayService.addPeripheralDevice(serialNumber, device);
        return ResponseEntity.status(HttpStatus.OK).body(updatedGateway.data());
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> removeDevice(@RequestParam String serialNumber, @PathVariable Long id) throws GatewayNotFoundException, DeviceNotFoundException {
        gatewayService.removePeripheralDevice(serialNumber, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
