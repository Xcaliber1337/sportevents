package com.example.sportevents.service;

import com.example.sportevents.model.SportType;
import com.example.sportevents.repository.SportTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportTypeService {

    private final SportTypeRepository repository;

    public SportTypeService(SportTypeRepository repository) {
        this.repository = repository;
    }

    public List<SportType> getAllSportTypes() {
        return repository.findAll();
    }
}