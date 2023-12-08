package com.codecharlan.gateway.service;

import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.repository.DeviceRepository;
import com.codecharlan.gateway.service.serviceimpl.ConnectivityChecker;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HttpBasedChecker implements ConnectivityChecker {

    private static final HttpClient httpClient = HttpClientBuilder.create().build();
    private final DeviceRepository deviceRepository;

    @Override
    public boolean performConnectivityCheck(PeripheralDevice device) {
        HttpGet request = new HttpGet("http://" + device.getGateway().getIpAddress());
        try {
            HttpResponse response = httpClient.execute(request);
            return response.getStatusLine().getStatusCode() == HttpStatus.OK.value();
        } catch (IOException e) {
            return false;
        } finally {
            request.abort();
        }
    }

    public void updateDeviceStatus() {
        try (CloseableHttpClient ignored1 = HttpClientBuilder.create().build()) {
            deviceRepository.findAll().forEach(device -> {
                try {
                    boolean isDeviceOnline = performConnectivityCheck(device);
                    device.setStatus(isDeviceOnline);
                    deviceRepository.save(device);
                } catch (Exception ignored) {
                }
            });
        } catch (IOException ignored) {
        }
    }
}

