package com.airplanecompany.admin.dao;

import com.airplanecompany.admin.entity.Pilot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PilotDao extends JpaRepository<Pilot, Long> {

    @Query(value = "select p from Pilot  as p where p.firstName like %:name% or p.lastName like %:name%")
    Page<Pilot> findPilotsByName(@Param("name") String name, PageRequest pageRequest);

    @Query(value = "select p from Pilot as p where p.user.email=:email")
    Pilot findPilotByEmail(@Param("email") String email);
}
