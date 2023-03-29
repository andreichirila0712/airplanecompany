package com.airplanecompany.admin.web;

import com.airplanecompany.admin.dto.FlightDTO;
import com.airplanecompany.admin.dto.PilotDTO;
import com.airplanecompany.admin.entity.User;
import com.airplanecompany.admin.service.FlightService;
import com.airplanecompany.admin.service.PilotService;
import com.airplanecompany.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pilots")
@CrossOrigin("*")
public class PilotRestController {
    private PilotService pilotService;
    private UserService userService;
    private FlightService flightService;

    public PilotRestController(PilotService pilotService, UserService userService, FlightService flightService) {
        this.pilotService = pilotService;
        this.userService = userService;
        this.flightService = flightService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<PilotDTO> searchPilots(@RequestParam(name="keyword", defaultValue = "") String keyword,
                                       @RequestParam(name="page", defaultValue = "0") int page,
                                       @RequestParam(name="size", defaultValue = "5") int size) {
        return pilotService.findPilotsByName(keyword, page, size);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Admin')")
    public List<PilotDTO> findAllPilots() {
        return pilotService.fetchPilots();
    }

    @DeleteMapping("/{pilotId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deletePilot(@PathVariable Long pilotId) {
        pilotService.removePilot(pilotId);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('Admin')")
    public PilotDTO savePilot(@RequestBody PilotDTO pilotDTO) {
        User user = userService.loadUserByEmail(pilotDTO.getUserDTO().getEmail());
        if (user != null) throw new RuntimeException("Email Already Exist");
        return pilotService.createPilot(pilotDTO);
    }

    @PutMapping("/{pilotId}")
    @PreAuthorize("hasAuthority('Pilot')")
    public PilotDTO updatePilot(@RequestBody PilotDTO pilotDTO, @PathVariable Long pilotId) {
        pilotDTO.setPilotId(pilotId);
        return pilotService.updatePilot(pilotDTO);
    }

    @GetMapping("/{pilotId}/flights")
    @PreAuthorize("hasAnyAuthority('Admin', 'Pilot')")
    public Page<FlightDTO> flightsByPilotId(@PathVariable Long pilotId,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "5") int size) {
        return flightService.fetchFlightsForPilot(pilotId, page, size);
    }

    @GetMapping("/find")
    @PreAuthorize("hasAuthority('Pilot')")
    public PilotDTO loadPilotByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return pilotService.loadPilotByEmail(email);
    }

}
