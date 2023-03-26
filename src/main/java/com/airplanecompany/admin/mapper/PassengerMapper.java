package com.airplanecompany.admin.mapper;

import com.airplanecompany.admin.dto.PassengerDTO;
import com.airplanecompany.admin.entity.Passenger;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PassengerMapper {

    public PassengerDTO fromPassenger(Passenger passenger) {
        PassengerDTO passengerDTO = new PassengerDTO();
        BeanUtils.copyProperties(passenger, passengerDTO);
        return passengerDTO;
    }

    public Passenger fromPassengerDTO(PassengerDTO passengerDTO) {
        Passenger passenger = new Passenger();
        BeanUtils.copyProperties(passengerDTO, passenger);
        return passenger;
    }
}
