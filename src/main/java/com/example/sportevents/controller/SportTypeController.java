package com.example.sportevents.controller;

import com.example.sportevents.model.SportType;
import com.example.sportevents.service.SportTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/sport-types")
public class SportTypeController {

    private final SportTypeService sportTypeService;

    public SportTypeController(SportTypeService sportTypeService) {
        this.sportTypeService = sportTypeService;
    }

    @GetMapping
    public ResponseEntity<List<SportType>> getAllSportTypes() {
        List<SportType> sportTypes = sportTypeService.getAllSportTypes();
        return ResponseEntity.ok(sportTypes);
    }
}