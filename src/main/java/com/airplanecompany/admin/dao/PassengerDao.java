package com.airplanecompany.admin.dao;

import com.airplanecompany.admin.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PassengerDao extends JpaRepository<Passenger, Long> {
    @Query(value = "select p from Passenger as p where p.firstName like %:name% or p.lastName like %:name%")
    Page<Passenger> findPassengersByName(@Param("name") String name, PageRequest pageRequest);

    @Query(value = "select p from Passenger as p where p.user.email=:email")
    Passenger findPassengerByEmail(@Param("email") String email);
}
