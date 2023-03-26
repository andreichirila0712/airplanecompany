package com.airplanecompany.admin.dao;

import com.airplanecompany.admin.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FlightDao extends JpaRepository<Flight, Long> {

    Page<Flight> findFlightByAirlineNameContains(String keyword, Pageable pageable);


    @Query(value = "select * from flights as f where f.flight_id in (select b.flight_id from board_in as b where b.passenger_id=:passengerId)", nativeQuery = true)
    Page<Flight> getFlightsByPassengerId(@Param("passengerId") Long passengerId, Pageable pageable);

    @Query(value = "select * from flights as f where f.flight_id not in (select b.flight_id from board_in as b where b.passenger_id=:passengerId)", nativeQuery = true)
    Page<Flight> getNotBoardInByPassengerId(@Param("passengerId") Long passengerId, Pageable pageable);

    @Query(value = "select f from Flight as f where f.pilot.pilotId=:pilotId")
    Page<Flight> getFlightsByPilotId(@Param("pilotId") Long pilotId, Pageable pageable);

}
