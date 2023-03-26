package com.airplanecompany.admin.service;
import com.airplanecompany.admin.dto.PilotDTO;
import com.airplanecompany.admin.entity.Pilot;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PilotService {
    Pilot loadPilotById(Long pilotId);
    Page<PilotDTO> findPilotsByName(String name, int page, int size);
    PilotDTO loadPilotByEmail(String email);
    PilotDTO createPilot(PilotDTO pilotDTO);
    PilotDTO updatePilot(PilotDTO pilotDTO);
    List<PilotDTO> fetchPilots();
    void removePilot(Long pilotId);
}
