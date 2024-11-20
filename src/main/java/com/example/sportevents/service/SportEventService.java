package com.example.sportevents.service;

import com.example.sportevents.dto.SportEventDto;
import com.example.sportevents.exception.InvalidSportTypeException;
import com.example.sportevents.exception.InvalidStatusException;
import com.example.sportevents.exception.InvalidStatusTransitionException;
import com.example.sportevents.exception.SportEventNotFoundException;
import com.example.sportevents.model.EventStatus;
import com.example.sportevents.model.SportEvent;
import com.example.sportevents.model.SportType;
import com.example.sportevents.repository.SportEventRepository;
import com.example.sportevents.repository.SportTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SportEventService {

    private static final Logger logger = LoggerFactory.getLogger(SportEventService.class);

    private final SportEventRepository repository;
    private final SportTypeRepository sportTypeRepository;
    private final Clock clock;

    public SportEventService(SportEventRepository repository, SportTypeRepository sportTypeRepository, Clock clock) {
        this.repository = repository;
        this.sportTypeRepository = sportTypeRepository;
        this.clock = clock;
    }

    public void deleteSportEvent(Long id) {
        SportEvent sportEvent = repository.findById(id)
                .orElseThrow(() -> new SportEventNotFoundException("Sport event not found with id: " + id));
        repository.delete(sportEvent);
    }

    public SportEventDto createSportEvent(SportEventDto sportEventDTO) {
        logger.debug("Creating new sport event: {}", sportEventDTO);

        SportType sportType = sportTypeRepository.findById(sportEventDTO.getSportTypeId())
                .orElseThrow(() -> new InvalidSportTypeException("Invalid sport type"));

        SportEvent sportEvent = new SportEvent();
        sportEvent.setName(sportEventDTO.getName());
        sportEvent.setSportType(sportType);
        sportEvent.setStartTime(sportEventDTO.getStartTime());
        sportEvent.setStatus(EventStatus.INACTIVE);

        SportEvent savedEvent = repository.save(sportEvent);
        logger.info("Sport event created with ID: {}", savedEvent.getId());

        return convertToDTO(savedEvent);
    }

    public void changeSportEventStatus(Long id, String statusStr) {
        logger.debug("Changing status of sport event ID {}: new status {}", id, statusStr);

        SportEvent sportEvent = repository.findById(id)
                .orElseThrow(() -> new SportEventNotFoundException("Sport event not found"));

        EventStatus newStatus;
        try {
            newStatus = EventStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusTransitionException("Invalid status provided: " + statusStr);
        }

        EventStatus currentStatus = sportEvent.getStatus();
        LocalDateTime now = LocalDateTime.now(clock);

        if (currentStatus == EventStatus.FINISHED) {
            throw new InvalidStatusTransitionException("Finished events cannot change status");
        }

        switch (newStatus) {
            case ACTIVE -> {
                if (currentStatus != EventStatus.INACTIVE) {
                    throw new InvalidStatusTransitionException("Can only change status from INACTIVE to ACTIVE");
                }
                if (sportEvent.getStartTime().isBefore(now)) {
                    throw new InvalidStatusTransitionException("Cannot activate an event if start time is in the past");
                }
            }
            case FINISHED -> {
                if (currentStatus != EventStatus.ACTIVE) {
                    throw new InvalidStatusTransitionException("Can only change status from ACTIVE to FINISHED");
                }
            }
            default -> throw new InvalidStatusTransitionException("Invalid status transition");
        }

        sportEvent.setStatus(newStatus);
        repository.save(sportEvent);
        logger.info("Status of sport event ID {} changed to {}", id, newStatus);
    }

    public List<SportEventDto> getSportEvents(String statusStr, String sportTypeName) {
        logger.debug("Fetching sport events with status: {}, sportType: {}", statusStr, sportTypeName);

        EventStatus status = null;
        if (statusStr != null) {
            try {
                status = EventStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidStatusException("Invalid status provided");
            }
        }

        List<SportEvent> events;

        if (status != null && sportTypeName != null) {
            events = repository.findByStatusAndSportType_NameIgnoreCase(status, sportTypeName);
        } else if (status != null) {
            events = repository.findByStatus(status);
        } else if (sportTypeName != null) {
            events = repository.findBySportType_NameIgnoreCase(sportTypeName);
        } else {
            events = repository.findAll();
        }

        return events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SportEventDto getSportEventById(Long id) {
        logger.debug("Fetching sport event by ID: {}", id);

        SportEvent sportEvent = repository.findById(id)
                .orElseThrow(() -> new SportEventNotFoundException("Sport event not found"));

        return convertToDTO(sportEvent);
    }

    private SportEventDto convertToDTO(SportEvent sportEvent) {
        SportEventDto dto = new SportEventDto();
        dto.setId(sportEvent.getId());
        dto.setName(sportEvent.getName());
        dto.setSportTypeId(sportEvent.getSportType().getId());
        dto.setSportTypeName(sportEvent.getSportType().getName());
        dto.setStatus(sportEvent.getStatus().name());
        dto.setStartTime(sportEvent.getStartTime());
        return dto;
    }
}
