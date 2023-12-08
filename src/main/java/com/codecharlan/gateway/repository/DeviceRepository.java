package com.codecharlan.gateway.repository;

import com.codecharlan.gateway.entity.PeripheralDevice;
import com.codecharlan.gateway.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<PeripheralDevice, Long> {
    Optional<PeripheralDevice> findByIdAndGateway(Long id, Gateway gateway);

}
