package com.airplanecompany.admin.service;

import com.airplanecompany.admin.dto.PassengerDTO;
import com.airplanecompany.admin.entity.Passenger;
import org.springframework.data.domain.Page;

public interface PassengerService {

    Passenger loadPassengerById(Long passengerId);
    Page<PassengerDTO> loadPassengersByName(String name, int page, int size);
    PassengerDTO loadPassengerByEmail(String email);
    PassengerDTO createPassenger(PassengerDTO passengerDTO);
    PassengerDTO updatePassenger(PassengerDTO passengerDTO);
    void removePassenger(Long passengerId);
}
