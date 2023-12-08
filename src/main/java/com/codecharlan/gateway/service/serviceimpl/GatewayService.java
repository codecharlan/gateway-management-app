package com.codecharlan.gateway.service.serviceimpl;

import com.codecharlan.gateway.dto.request.GatewayRequest;
import com.codecharlan.gateway.dto.response.ApiResponse;
import com.codecharlan.gateway.dto.response.GatewayResponse;
import com.codecharlan.gateway.entity.Gateway;
import com.codecharlan.gateway.entity.PeripheralDevice;

import java.util.List;

public interface GatewayService {
    ApiResponse<GatewayResponse> saveGateway(GatewayRequest gatewayRequest);
    ApiResponse<List<GatewayResponse>> getAllGateways();
    ApiResponse<Gateway> getGatewayBySerialNumber(String serialNumber);
    ApiResponse<GatewayResponse> addPeripheralDevice(String serialNumber, PeripheralDevice peripheralDevice);
    ApiResponse<?> removePeripheralDevice(String serialNumber, Long uid);
}