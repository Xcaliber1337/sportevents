package com.example.sportevents.repository;

import com.example.sportevents.model.SportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportTypeRepository extends JpaRepository<SportType, Long> {
    Optional<SportType> findByNameIgnoreCase(String name);
}