package com.airplanecompany.admin.mapper;

import com.airplanecompany.admin.dto.PilotDTO;
import com.airplanecompany.admin.entity.Pilot;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PilotMapper {

    public PilotDTO fromPilot(Pilot pilot) {
        PilotDTO pilotDTO = new PilotDTO();
        BeanUtils.copyProperties(pilot, pilotDTO);
        return pilotDTO;
    }

    public Pilot fromPilotDTO(PilotDTO pilotDTO) {
        Pilot pilot = new Pilot();
        BeanUtils.copyProperties(pilotDTO, pilot);
        return pilot;
    }
}
