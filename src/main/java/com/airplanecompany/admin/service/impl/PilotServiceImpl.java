package com.airplanecompany.admin.service.impl;

import com.airplanecompany.admin.dao.PilotDao;
import com.airplanecompany.admin.dto.PilotDTO;
import com.airplanecompany.admin.entity.Flight;
import com.airplanecompany.admin.entity.Pilot;
import com.airplanecompany.admin.entity.User;
import com.airplanecompany.admin.mapper.PilotMapper;
import com.airplanecompany.admin.service.FlightService;
import com.airplanecompany.admin.service.PilotService;
import com.airplanecompany.admin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PilotServiceImpl implements PilotService {

    private PilotDao pilotDao;
    private PilotMapper pilotMapper;
    private UserService userService;
    private FlightService flightService;

    public PilotServiceImpl(PilotDao pilotDao, PilotMapper pilotMapper, UserService userService, FlightService flightService) {
        this.pilotDao = pilotDao;
        this.pilotMapper = pilotMapper;
        this.userService = userService;
        this.flightService = flightService;
    }

    @Override
    public Pilot loadPilotById(Long pilotId) {
        return pilotDao.findById(pilotId).orElseThrow(() -> new EntityNotFoundException("Pilot with ID " + pilotId + " Not Found"));
    }

    @Override
    public Page<PilotDTO> findPilotsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Pilot> pilotsPage = pilotDao.findPilotsByName(name, pageRequest);
        return new PageImpl<>(pilotsPage.getContent().stream().map(pilot -> pilotMapper.fromPilot(pilot)).collect(Collectors.toList()), pageRequest, pilotsPage.getTotalElements());
    }

    @Override
    public PilotDTO loadPilotByEmail(String email) {
        return pilotMapper.fromPilot(pilotDao.findPilotByEmail(email));
    }

    @Override
    public PilotDTO createPilot(PilotDTO pilotDTO) {
        User user = userService.createUser(pilotDTO.getUserDTO().getEmail(),pilotDTO.getUserDTO().getPassword());
        userService.assignRoleToUser(user.getEmail(),"Pilot");
        Pilot pilot = pilotMapper.fromPilotDTO(pilotDTO);
        pilot.setUser(user);
        Pilot savedPilot = pilotDao.save(pilot);
        return pilotMapper.fromPilot(savedPilot);
    }

    @Override
    public PilotDTO updatePilot(PilotDTO pilotDTO) {
        Pilot loadedPilot = loadPilotById(pilotDTO.getPilotId());
        Pilot pilot = pilotMapper.fromPilotDTO(pilotDTO);
        pilot.setUser(loadedPilot.getUser());
        pilot.setFlights(loadedPilot.getFlights());
        Pilot updatedPilot = pilotDao.save(pilot);
        return pilotMapper.fromPilot(updatedPilot);
    }

    @Override
    public List<PilotDTO> fetchPilots() {
        return pilotDao.findAll().stream().map(pilot -> pilotMapper.fromPilot(pilot)).collect(Collectors.toList());
    }

    @Override
    public void removePilot(Long pilotId) {
        Pilot pilot = loadPilotById(pilotId);
        for(Flight flight : pilot.getFlights()) {
            flightService.removeFlight(flight.getFlightId());
        }
        pilotDao.deleteById(pilotId);

    }
}
