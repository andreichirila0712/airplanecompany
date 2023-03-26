package com.airplanecompany.admin.mapper;

import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.entity.Flight;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class FlightMapper {

    private PilotMapper pilotMapper;

    public FlightMapper(PilotMapper pilotMapper) {
        this.pilotMapper = pilotMapper;
    }

    public FlightDTO fromFlight(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        BeanUtils.copyProperties(flight, flightDTO);
        flightDTO.setPilotDTO(pilotMapper.fromPilot(flight.getPilot()));
        return flightDTO;
    }

    public Flight fromFlightDTO(FlightDTO flightDTO) {
        Flight flight = new Flight();
        BeanUtils.copyProperties(flightDTO, flight);
        return flight;
    }
}
