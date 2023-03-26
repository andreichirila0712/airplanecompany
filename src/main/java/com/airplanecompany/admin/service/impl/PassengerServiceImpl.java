package com.airplanecompany.admin.service.impl;

import com.airplanecompany.admin.dao.PassengerDao;
import com.airplanecompany.admin.dto.PassengerDTO;
import com.airplanecompany.admin.entity.Flight;
import com.airplanecompany.admin.entity.Passenger;
import com.airplanecompany.admin.entity.User;
import com.airplanecompany.admin.mapper.PassengerMapper;
import com.airplanecompany.admin.service.PassengerService;
import com.airplanecompany.admin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@Transactional
public class PassengerServiceImpl implements PassengerService {
    private PassengerDao passengerDao;
    private PassengerMapper passengerMapper;
    private UserService userService;

    public PassengerServiceImpl(PassengerDao passengerDao, PassengerMapper passengerMapper, UserService userService) {
        this.passengerDao = passengerDao;
        this.passengerMapper = passengerMapper;
        this.userService = userService;
    }

    @Override
    public Passenger loadPassengerById(Long passengerId) {
        return passengerDao.findById(passengerId).orElseThrow(() -> new EntityNotFoundException("Passenger with ID" + passengerId + " Not Found"));
    }

    @Override
    public Page<PassengerDTO> loadPassengersByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Passenger> passengersPage = passengerDao.findPassengersByName(name, pageRequest);
        return new PageImpl<>(passengersPage.getContent().stream().map(passenger -> passengerMapper.fromPassenger(passenger)).collect(Collectors.toList()), pageRequest, passengersPage.getTotalElements());
    }

    @Override
    public PassengerDTO loadPassengerByEmail(String email) {
        return passengerMapper.fromPassenger(passengerDao.findPassengerByEmail(email));

    }

    @Override
    public PassengerDTO createPassenger(PassengerDTO passengerDTO) {
        User user = userService.createUser(passengerDTO.getUserDTO().getEmail(), passengerDTO.getUserDTO().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Passenger");
        Passenger passenger = passengerMapper.fromPassengerDTO(passengerDTO);
        passenger.setUser(user);
        Passenger savedPassenger = passengerDao.save(passenger);
        return passengerMapper.fromPassenger(savedPassenger);

    }

    @Override
    public PassengerDTO updatePassenger(PassengerDTO passengerDTO) {
        Passenger loadedPassenger = loadPassengerById(passengerDTO.getPassengerId());
        Passenger passenger = passengerMapper.fromPassengerDTO(passengerDTO);
        passenger.setUser(loadedPassenger.getUser());
        passenger.setFlights(loadedPassenger.getFlights());
        Passenger updatedPassenger = passengerDao.save(passenger);
        return passengerMapper.fromPassenger(updatedPassenger);
    }

    @Override
    public void removePassenger(Long passengerId) {
        Passenger passenger = loadPassengerById(passengerId);
        Iterator<Flight> flightIterator = passenger.getFlights().iterator();
        if(flightIterator.hasNext()) {
            Flight flight = flightIterator.next();
            flight.removePassengerFromFlight(passenger);
        }
        passengerDao.deleteById(passengerId);

    }
}
