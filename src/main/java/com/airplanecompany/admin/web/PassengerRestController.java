package com.airplanecompany.admin.web;

import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.dto.PassengerDTO;
import com.airplanecompany.admin.entity.User;
import com.airplanecompany.admin.service.FlightService;
import com.airplanecompany.admin.service.PassengerService;
import com.airplanecompany.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passengers")
@CrossOrigin("*")
public class PassengerRestController {

    private PassengerService passengerService;
    private UserService userService;
    private FlightService flightService;

    public PassengerRestController(PassengerService passengerService, UserService userService, FlightService flightService) {
        this.passengerService = passengerService;
        this.userService = userService;
        this.flightService = flightService;
    }

    @GetMapping
    public Page<PassengerDTO> searchPassengers(@RequestParam(name="keyword", defaultValue = "") String keyword,
                                               @RequestParam(name="page", defaultValue = "0") int page,
                                               @RequestParam(name="size", defaultValue = "5") int size) {
        return passengerService.loadPassengersByName(keyword, page, size);
    }

    @DeleteMapping("/{passengerId}")
    public void deletePassenger(@PathVariable Long passengerId) {
        passengerService.removePassenger(passengerId);
    }

    @PostMapping
    public PassengerDTO savePassenger(@RequestBody PassengerDTO passengerDTO) {
        User user = userService.loadUserByEmail(passengerDTO.getUserDTO().getEmail());
        if(user!=null) throw new RuntimeException("Email Already Exists");
        return passengerService.createPassenger(passengerDTO);
    }

    @PutMapping("/{passengerId}")
    public PassengerDTO updatePassenger(@RequestBody PassengerDTO passengerDTO, @PathVariable Long passengerId) {
        passengerDTO.setPassengerId(passengerId);
        return passengerService.updatePassenger(passengerDTO);
    }

    @GetMapping("/{passengerId}/flights")
    public Page<FlightDTO> flightsByPassengerId(@PathVariable Long passengerId,
                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "5") int size) {
        return flightService.fetchFlightsForPassenger(passengerId, page, size);
    }

    @GetMapping("/{passengerId}/other-flights")
    public Page<FlightDTO> nonBoardedFlightsByPassengerId(@PathVariable Long passengerId,
                                                          @RequestParam(name="page", defaultValue = "0") int page,
                                                          @RequestParam(name="size", defaultValue = "5") int size) {
        return flightService.fetchNotBoardInFlightsForPassenger(passengerId, page, size);
    }

    @GetMapping("/find")
    public PassengerDTO loadPassengerByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return passengerService.loadPassengerByEmail(email);

    }
}
