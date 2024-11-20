package com.example.sportevents.controller;

import com.example.sportevents.dto.SportEventDto;
import com.example.sportevents.dto.StatusChangeRequest;
import com.example.sportevents.service.SportEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sport-events")
public class SportEventController {

    private final SportEventService service;

    public SportEventController(SportEventService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSportEvent(@PathVariable Long id) {
        service.deleteSportEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SportEventDto> createSportEvent(@Valid @RequestBody SportEventDto sportEventDTO) {
        SportEventDto createdEvent = service.createSportEvent(sportEventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping
    public ResponseEntity<List<SportEventDto>> getSportEvents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sportType) {
        List<SportEventDto> events = service.getSportEvents(status, sportType);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportEventDto> getSportEventById(@PathVariable Long id) {
        SportEventDto eventDTO = service.getSportEventById(id);
        return ResponseEntity.ok(eventDTO);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeSportEventStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusChangeRequest request) {
        service.changeSportEventStatus(id, request.getStatus());
        return ResponseEntity.noContent().build();
    }
}