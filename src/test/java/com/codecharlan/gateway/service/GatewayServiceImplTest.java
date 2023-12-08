package com.codecharlan.gateway.service;

import com.codecharlan.gateway.dto.request.GatewayRequest;
import com.codecharlan.gateway.dto.response.ApiResponse;
import com.codecharlan.gateway.dto.response.GatewayResponse;
import com.codecharlan.gateway.entity.Gateway;
import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.exception.GatewayNotFoundException;
import com.codecharlan.gateway.exception.MaximumDevicesReachedException;
import com.codecharlan.gateway.repository.GatewayRepository;
import com.codecharlan.gateway.service.serviceimpl.DeviceService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.codecharlan.gateway.service.GatewayServiceImpl.fromGateway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GatewayServiceImplTest {
    @Mock
    private GatewayRepository gatewayRepository;
    @Mock
    private DeviceService deviceService;
    @InjectMocks
    private GatewayServiceImpl gatewayService;
    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveGateway_validRequest_returnsSavedGateway() {
        GatewayRequest request = new GatewayRequest("ABC123", "My Gateway", "192.168.1.1", LocalDateTime.now());
        Gateway gateway = Gateway.builder()
                .serialNumber(request.getSerialNumber())
                .name(request.getName())
                .ipAddress(request.getIpAddress())
                .createdAt(request.getCreatedAt())
                .build();

        when(gatewayRepository.save(Mockito.any())).thenReturn(gateway);

        ApiResponse<GatewayResponse> response = gatewayService.saveGateway(request);

        assertEquals(HttpStatus.CREATED.value(), response.status());
        assertNotNull(response.data());
        assertThat(response.data()).isEqualTo(fromGateway(gateway));
    }


    @Test
    public void getAllGateways_gatewaysExist_returnsListOfGateways() {
        List<Gateway> gateways = new ArrayList<>();
        gateways.add(Gateway.builder().serialNumber("ABC123").name("Gateway 1").build());
        gateways.add(Gateway.builder().serialNumber("DEF456").name("Gateway 2").build());
        when(gatewayRepository.findAll()).thenReturn(gateways);

        ApiResponse<List<GatewayResponse>> response = gatewayService.getAllGateways();

        assertEquals(HttpStatus.OK.value(), response.status());
        assertNotNull(response.data());
        assertEquals(2, response.data().size());
    }

    @Test
    public void getAllGateways_noGatewaysExist_returnsEmptyList() {
        when(gatewayRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<List<GatewayResponse>> response = gatewayService.getAllGateways();

        assertEquals(HttpStatus.OK.value(), response.status());
        assertNotNull(response.data());
        assertTrue(response.data().isEmpty());
    }

    @Test
    public void getGatewayBySerialNumber_existingGateway_returnsGateway() {
        String serialNumber = "ABC123";
        Gateway gateway = Gateway.builder().serialNumber(serialNumber).build();
        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(gateway));

        ApiResponse<Gateway> response = gatewayService.getGatewayBySerialNumber(serialNumber);

        assertEquals(HttpStatus.OK.value(), response.status());
        assertNotNull(response.data());
        assertEquals(serialNumber, response.data().getSerialNumber());
    }

    @Test
    public void getGatewayBySerialNumber_nonExistentGateway_throwsException() {
        String serialNumber = "XYZ789";
        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());

        assertThrows(GatewayNotFoundException.class, () -> gatewayService.getGatewayBySerialNumber(serialNumber));
    }

    @Test
    public void addPeripheralDevice_deviceNotFound_throwsException() {
        String serialNumber = "ABC123";
        PeripheralDevice device = null;

        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());

        assertThrows(GatewayNotFoundException.class, () -> gatewayService.addPeripheralDevice(serialNumber, device));

    }


    @Test
    public void addPeripheralDevice_maximumDevicesReached_throwsException() {
        String serialNumber = "DEF456";
        PeripheralDevice device = PeripheralDevice.builder().uid(654321).vendor("XYZ").build();

        List<PeripheralDevice> devices = new ArrayList<>();
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        devices.add(new PeripheralDevice());
        Gateway gateway = Gateway.builder().serialNumber(serialNumber).peripheralDevices(devices).build();
        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(gateway));

        assertThrows(MaximumDevicesReachedException.class, () -> gatewayService.addPeripheralDevice(serialNumber, device));
        verifyNoInteractions(deviceService);
    }

    @Test
    public void removePeripheralDevice_existingDevice_removesDeviceAndUpdateGateway() {
        String serialNumber = "GHI789";
        Long deviceId = 1L;

        List<PeripheralDevice> devices = new ArrayList<>();
        PeripheralDevice deviceToRemove = new PeripheralDevice();
        deviceToRemove.setId(deviceId);
        devices.add(new PeripheralDevice());
        devices.add(deviceToRemove);
        Gateway gateway = Gateway.builder().serialNumber(serialNumber).peripheralDevices(devices).build();

        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(gateway));

        doNothing().when(deviceService).deleteDevice(eq(gateway), eq(deviceId));

        ApiResponse<?> response = gatewayService.removePeripheralDevice(serialNumber, deviceId);

        assertEquals("Peripheral device removed successfully", response.message());
        assertEquals(HttpStatus.OK.value(), response.status());

        verify(deviceService, times(1)).deleteDevice(eq(gateway), eq(deviceId));
    }

}