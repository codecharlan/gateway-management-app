# The Gateway Application

![CI/CD Workflow](https://github.com/codecharlan/Test/actions/workflows/maven.yml/badge.svg)

## Overview

Welcome to the Gateway Application, a REST API service designed to for storing information about gateways and their associated devices. Our service provides an array of capabilities:

- **Storing New Gateway:** Seamlessly register new gateway.
- **Displays Gateways Information:** Displaying information about all stored gateways (and their devices)
- **Display a Single Gateway Info:** Displaying details about a single gateway.
- **Add and Remove Peripheral Device:** Adding and removing a peripheral device from a gateway.

### Prerequisites

Ensure that input and output data are formatted in JSON.

### Getting Started

To begin using the Gateway Application, follow these steps:

1. Install the required dependencies by running:
   ```shell
   mvn install
    ```

2. Start the application with:
    ```shell
    mvn spring-boot:run
    ```

### API Endpoints
The API exposes the following endpoints:

* **Storing New Gateway:** Send a POST request to **`/api/gateway/save`** with a JSON request body including name, serial number and ipAddress.

* **Displays Gateways Information:** Issue a GET request to **`/api/gateway/all`**.

* **Display a Single Gateway Info:** Issue a GET request to **`/api/gateway/{serialNumber}`**.

* **Add Peripheral Device:** Add device to a gateway with a PUT request to **`/api/gateway/devices/add?serialNumber=${serialNumber}`**.

* **Remove Peripheral Device:** Remove Device from Gateway with a DELETE request to **`/api/gateway/devices/${deviceId}?serialNumber=${serialNumber}`**.

### Testing
Unit tests can be run using the following command:

```shell
    mvn test
   ```
### Technology Used:
* Java
* SpringBoot
* CI/CD
* Junit & Mockito
* Git and GitHub

### Assumptions
Online/Offline Status:
The "status" attribute reflects the current connectivity state of a peripheral device, indicating whether it's "online" or "offline."
This status dynamically changes based on the device's connectivity to the gateway or network.
Connectivity Check:
The system employs a dedicated mechanism to monitor device connectivity.
A scheduled task or heartbeat mechanism performs periodic checks, typically every 5 minutes, to assess device availability.
This routine verifies the connectivity status of devices and updates their "online" or "offline" state accordingly.

### Conclusion
In conclusion, our REST API service offers a robust solution for gateway management system and peripheral devices.
We encourage you to explore the endpoints and functionalities provided by our API for a detailed guide on using our service effectively. Whether you're a developer or a user, our service is designed to simplify gateway management 
tasks and enhance operational efficiency.

Feel free to reach out with any questions, feedback, or suggestions.