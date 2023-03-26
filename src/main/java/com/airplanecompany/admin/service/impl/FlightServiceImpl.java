package com.airplanecompany.admin.service.impl;

import com.airplanecompany.admin.dao.FlightDao;
import com.airplanecompany.admin.dao.PassengerDao;
import com.airplanecompany.admin.dao.PilotDao;
import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.dto.PassengerDTO;
import com.airplanecompany.admin.entity.Flight;
import com.airplanecompany.admin.entity.Passenger;
import com.airplanecompany.admin.entity.Pilot;
import com.airplanecompany.admin.mapper.FlightMapper;
import com.airplanecompany.admin.service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private FlightDao flightDao;
    private FlightMapper flightMapper;
    private PilotDao pilotDao;
    private PassengerDao passengerDao;

    public FlightServiceImpl(FlightDao flightDao, FlightMapper flightMapper, PilotDao pilotDao, PassengerDao passengerDao) {
        this.flightDao = flightDao;
        this.flightMapper = flightMapper;
        this.pilotDao = pilotDao;
        this.passengerDao = passengerDao;
    }

    @Override
    public Flight loadFlightById(Long flightId) {
        return flightDao.findById(flightId).orElseThrow(() -> new EntityNotFoundException("Flight with ID " + flightId + " Not Found"));
    }

    @Override
    public FlightDTO createFlight(FlightDTO flightDTO) {
        Flight flight = flightMapper.fromFlightDTO(flightDTO);
        Pilot pilot = pilotDao.findById(flightDTO.getPilotDTO().getPilotId()).orElseThrow(() -> new EntityNotFoundException("Pilot with ID " + flightDTO.getPilotDTO().getPilotId() + " Not Found"));
        flight.setPilot(pilot);
        Flight savedFlight = flightDao.save(flight);
        return flightMapper.fromFlight(savedFlight);
    }

    @Override
    public FlightDTO updateFlight(FlightDTO flightDTO) {
        Flight loadedFlight = loadFlightById(flightDTO.getFlightId());
        Pilot pilot = pilotDao.findById(flightDTO.getPilotDTO().getPilotId()).orElseThrow(() -> new EntityNotFoundException("Pilot with ID " + flightDTO.getPilotDTO().getPilotId() + " Not Found"));
        Flight flight = flightMapper.fromFlightDTO(flightDTO);
        flight.setPilot(pilot);
        flight.setPassengers(loadedFlight.getPassengers());
        Flight updatedFlight = flightDao.save(flight);
        return flightMapper.fromFlight(updatedFlight);
    }

    @Override
    public Page<FlightDTO> findFlightByFlightName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Flight> flightsPage = flightDao.findFlightByAirlineNameContains(keyword, pageRequest);
        return new PageImpl<>(flightsPage.getContent().stream().map(flight -> flightMapper.fromFlight(flight)).collect(Collectors.toList()), pageRequest, flightsPage.getTotalElements());

    }

    @Override
    public void assignPassengerToFlight(Long flightId, Long passengerId) {
        Passenger passenger = passengerDao.findById(passengerId).orElseThrow(() -> new EntityNotFoundException("Passenger with ID " + passengerId + " Not Found"));
        Flight flight = loadFlightById(flightId);
        flight.assignPassengerToFlight(passenger);
    }

    @Override
    public Page<FlightDTO> fetchFlightsForPassenger(Long passengerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Flight> passengerFlightsPage = flightDao.getFlightsByPassengerId(passengerId, pageRequest);
        return new PageImpl<>(passengerFlightsPage.getContent().stream().map(flight -> flightMapper.fromFlight(flight)).collect(Collectors.toList()), pageRequest, passengerFlightsPage.getTotalElements());
    }

    @Override
    public Page<FlightDTO> fetchNotBoardInFlightsForPassenger(Long passengerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Flight> notBoardInFlightsPage = flightDao.getNotBoardInByPassengerId(passengerId, pageRequest);
        return new PageImpl<>(notBoardInFlightsPage.getContent().stream().map(flight -> flightMapper.fromFlight(flight)).collect(Collectors.toList()), pageRequest, notBoardInFlightsPage.getTotalElements());
    }

    @Override
    public void removeFlight(Long flightId) {
        flightDao.deleteById(flightId);
    }

    @Override
    public Page<FlightDTO> fetchFlightsForPilot(Long pilotId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Flight> pilotFlightsPage = flightDao.getFlightsByPilotId(pilotId, pageRequest);
        return new PageImpl<>(pilotFlightsPage.getContent().stream().map(flight -> flightMapper.fromFlight(flight)).collect(Collectors.toList()), pageRequest, pilotFlightsPage.getTotalElements());
    }
}
