package com.example.sportevents.repository;


import com.example.sportevents.model.EventStatus;
import com.example.sportevents.model.SportEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportEventRepository extends JpaRepository<SportEvent, Long> {
    List<SportEvent> findByStatus(EventStatus status);
    List<SportEvent> findBySportType_NameIgnoreCase(String sportTypeName);
    List<SportEvent> findByStatusAndSportType_NameIgnoreCase(EventStatus status, String sportTypeName);
}