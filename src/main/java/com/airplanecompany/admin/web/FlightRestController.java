package com.airplanecompany.admin.web;

import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FlightRestController {
    private FlightService flightService;

    public FlightRestController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public Page<FlightDTO> searchFlights(@RequestParam(name="keyword", defaultValue = "") String keyword,
                                         @RequestParam(name="page", defaultValue = "0") int page,
                                         @RequestParam(name="size", defaultValue = "5") int size) {
        return flightService.findFlightByFlightName(keyword, page, size);
    }

    @DeleteMapping("/{flightId}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public void deleteFlight(@PathVariable Long flightId) {
        flightService.removeFlight(flightId);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('Admin', 'Pilot')")
    public FlightDTO saveFlight(@RequestBody FlightDTO flightDTO) {
        return flightService.createFlight(flightDTO);
    }

    @PutMapping("/{flightId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Pilot')")
    public FlightDTO updateFlight(@RequestBody FlightDTO flightDTO, @PathVariable Long flightId) {
        flightDTO.setFlightId(flightId);
        return flightService.updateFlight(flightDTO);
    }

    @PostMapping("/{flightId}/board/passengers/{passengerId}")
    @PreAuthorize("hasAuthority('Passenger')")
    public void boardPassengerInFlight(@PathVariable Long flightId, @PathVariable Long passengerId) {
        flightService.assignPassengerToFlight(flightId, passengerId);
    }
}
