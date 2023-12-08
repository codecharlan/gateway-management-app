package com.codecharlan.gateway.dto.response;

import com.codecharlan.gateway.entity.PeripheralDevice;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonSerialize
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GatewayResponse {
    private String serialNumber;
    private String name;
    private String ipAddress;
    private LocalDateTime createdAt;
    private List<PeripheralDevice> peripheralDevices;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayResponse that = (GatewayResponse) o;
        return Objects.equals(serialNumber, that.serialNumber) && Objects.equals(name, that.name) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(createdAt, that.createdAt) && Objects.equals(peripheralDevices, that.peripheralDevices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, name, ipAddress, createdAt, peripheralDevices);
    }
}
