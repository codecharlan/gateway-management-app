package com.codecharlan.gateway.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatewayRequest {
    private String serialNumber;
    private String name;
    private String ipAddress;
    private LocalDateTime createdAt;
}
