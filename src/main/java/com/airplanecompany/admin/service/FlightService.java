package com.airplanecompany.admin.service;

import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.entity.Flight;
import org.springframework.data.domain.Page;

public interface FlightService {

    Flight loadFlightById(Long flightId);
    FlightDTO createFlight(FlightDTO flightDTO);
    FlightDTO updateFlight(FlightDTO flightDTO);
    Page<FlightDTO> findFlightByFlightName(String keyword, int page, int size);
    void assignPassengerToFlight(Long flightId, Long passengerId);
    Page<FlightDTO> fetchFlightsForPassenger(Long passengerId, int page, int size);
    Page<FlightDTO> fetchNotBoardInFlightsForPassenger(Long passengerId, int page, int size);
    void removeFlight(Long flightId);
    Page<FlightDTO> fetchFlightsForPilot(Long pilotId,int page, int size);
}
