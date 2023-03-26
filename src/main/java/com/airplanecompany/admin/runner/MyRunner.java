package com.airplanecompany.admin.runner;

import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.dto.PassengerDTO;
import com.airplanecompany.admin.dto.PilotDTO;
import com.airplanecompany.admin.dto.UserDTO;
import com.airplanecompany.admin.entity.Passenger;
import com.airplanecompany.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PilotService pilotService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private PassengerService passengerService;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdmin();
        createPilots();
        createFlights();
        PassengerDTO passenger = createPassenger();
        assignFlightToPassenger(passenger);
        createPassengers();
    }

    private void createPassengers() {
        for(int i = 1; i < 10; i++) {
            PassengerDTO passengerDTO = new PassengerDTO();
            passengerDTO.setFirstName("passengerFN"+i);
            passengerDTO.setLastName("passengerLN"+i);
            passengerDTO.setPhoneNumber("075217911"+i);
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail("passenger"+i+"@gmail.com");
            userDTO.setPassword("1234");
            passengerDTO.setUserDTO(userDTO);
            passengerService.createPassenger(passengerDTO);
        }
    }

    private void assignFlightToPassenger(PassengerDTO passenger) {
        flightService.assignPassengerToFlight(1L, passenger.getPassengerId());
    }

    private PassengerDTO createPassenger() {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setFirstName("passengerFN");
        passengerDTO.setLastName("passengerLN");
        passengerDTO.setPhoneNumber("0752179113");
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("passenger@gmail.com");
        userDTO.setPassword("1234");
        passengerDTO.setUserDTO(userDTO);
        return passengerService.createPassenger(passengerDTO);
    }

    private void createFlights() {
        for (int i = 0; i < 20; i++) {
            FlightDTO flightDTO = new FlightDTO();
            flightDTO.setAirlineName("TAROM"+i);
            flightDTO.setToLocation("flight"+i+"TL");
            flightDTO.setFromLocation("flight"+i+"FL");
            flightDTO.setDepartureTime("flight"+i+"DT");
            flightDTO.setArrivalTime("flight"+i+"AT");
            flightDTO.setDuration("flight"+i+"D");
            flightDTO.setTotalSeats(i*10);
            PilotDTO pilotDTO = new PilotDTO();
            pilotDTO.setPilotId(1L);
            flightDTO.setPilotDTO(pilotDTO);
            flightService.createFlight(flightDTO);

        }
    }

    private void createPilots() {
        for(int i = 0; i < 10; i++) {
            PilotDTO pilotDTO = new PilotDTO();
            pilotDTO.setFirstName("pilot"+i+"FN");
            pilotDTO.setLastName("pilot"+i+"LN");
            pilotDTO.setSummary("advanced"+i);
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail("pilot"+i+"@gmail.com");
            userDTO.setPassword("1234");
            pilotDTO.setUserDTO(userDTO);
            pilotService.createPilot(pilotDTO);
        }
    }

    private void createAdmin() {
        userService.createUser("admin@gmail.com", "1234");
        userService.assignRoleToUser("admin@gmail.com", "Admin");
    }

    private void createRoles() {
        Arrays.asList("Admin", "Pilot", "Passenger").forEach(role-> roleService.createRole(role));
    }


}
