package com.codecharlan.gateway.repository;

import com.codecharlan.gateway.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GatewayRepository extends JpaRepository<Gateway, String> {
    Optional<Gateway> findBySerialNumber(String serialNumber);

}
