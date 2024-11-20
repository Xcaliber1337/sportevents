package com.example.sportevents.service;

import com.example.sportevents.dto.SportEventDto;
import com.example.sportevents.exception.*;
import com.example.sportevents.model.EventStatus;
import com.example.sportevents.model.SportEvent;
import com.example.sportevents.model.SportType;
import com.example.sportevents.repository.SportEventRepository;
import com.example.sportevents.repository.SportTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SportEventServiceTest {

    @InjectMocks
    private SportEventService sportEventService;

    @Mock
    private SportEventRepository sportEventRepository;

    @Mock
    private SportTypeRepository sportTypeRepository;

    private Clock clock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clock = Clock.fixed(Instant.parse("2023-10-01T10:00:00Z"), ZoneId.of("UTC"));
        sportEventService = new SportEventService(sportEventRepository, sportTypeRepository, clock);
    }

    @Test
    void createSportEvent_ValidData_Success() {
        // Arrange
        SportEventDto dto = new SportEventDto();
        dto.setName("Championship Match");
        dto.setSportTypeId(1L);
        dto.setStartTime(LocalDateTime.now(clock).plusDays(1));

        SportType sportType = new SportType();
        sportType.setId(1L);
        sportType.setName("FOOTBALL");

        when(sportTypeRepository.findById(1L)).thenReturn(Optional.of(sportType));

        SportEvent savedEvent = new SportEvent();
        savedEvent.setId(1L);
        savedEvent.setName(dto.getName());
        savedEvent.setSportType(sportType);
        savedEvent.setStartTime(dto.getStartTime());
        savedEvent.setStatus(EventStatus.INACTIVE);

        when(sportEventRepository.save(any(SportEvent.class))).thenReturn(savedEvent);

        // Act
        SportEventDto result = sportEventService.createSportEvent(dto);

        // Assert
        assertNotNull(result);
        assertEquals(savedEvent.getId(), result.getId());
        assertEquals(savedEvent.getName(), result.getName());
        assertEquals(savedEvent.getSportType().getId(), result.getSportTypeId());
        assertEquals(savedEvent.getStatus().name(), result.getStatus());
        verify(sportEventRepository, times(1)).save(any(SportEvent.class));
    }

    @Test
    void createSportEvent_InvalidSportType_ThrowsException() {
        // Arrange
        SportEventDto dto = new SportEventDto();
        dto.setName("Championship Match");
        dto.setSportTypeId(99L);
        dto.setStartTime(LocalDateTime.now(clock).plusDays(1));

        when(sportTypeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidSportTypeException.class, () -> sportEventService.createSportEvent(dto));
        verify(sportEventRepository, never()).save(any(SportEvent.class));
    }

    @Test
    void changeSportEventStatus_ValidTransition_Success() {
        // Arrange
        Long eventId = 1L;
        String newStatus = "ACTIVE";

        SportType sportType = new SportType();
        sportType.setId(1L);
        sportType.setName("FOOTBALL");

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(eventId);
        sportEvent.setName("Championship Match");
        sportEvent.setSportType(sportType);
        sportEvent.setStatus(EventStatus.INACTIVE);
        sportEvent.setStartTime(LocalDateTime.now(clock).plusDays(1));

        when(sportEventRepository.findById(eventId)).thenReturn(Optional.of(sportEvent));
        when(sportEventRepository.save(any(SportEvent.class))).thenReturn(sportEvent);

        // Act
        sportEventService.changeSportEventStatus(eventId, newStatus);

        // Assert
        assertEquals(EventStatus.ACTIVE, sportEvent.getStatus());
        verify(sportEventRepository, times(1)).save(sportEvent);
    }

    @Test
    void changeSportEventStatus_EventNotFound_ThrowsException() {
        // Arrange
        Long eventId = 1L;
        String newStatus = "ACTIVE";

        when(sportEventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SportEventNotFoundException.class, () -> sportEventService.changeSportEventStatus(eventId, newStatus));
        verify(sportEventRepository, never()).save(any(SportEvent.class));
    }

    @Test
    void changeSportEventStatus_InvalidStatus_ThrowsException() {
        // Arrange
        Long eventId = 1L;
        String newStatus = "UNKNOWN_STATUS";

        when(sportEventRepository.findById(eventId)).thenReturn(Optional.of(new SportEvent()));

        // Act & Assert
        assertThrows(InvalidStatusTransitionException.class, () -> sportEventService.changeSportEventStatus(eventId, newStatus));
        verify(sportEventRepository, never()).save(any(SportEvent.class));
    }

    @Test
    void getSportEvents_NoFilters_ReturnsAllEvents() {
        // Arrange
        SportType sportType = new SportType();
        sportType.setId(1L);
        sportType.setName("FOOTBALL");

        SportEvent event1 = new SportEvent();
        event1.setId(1L);
        event1.setName("Match 1");
        event1.setSportType(sportType);
        event1.setStatus(EventStatus.ACTIVE);
        event1.setStartTime(LocalDateTime.now(clock).plusDays(1));

        SportEvent event2 = new SportEvent();
        event2.setId(2L);
        event2.setName("Match 2");
        event2.setSportType(sportType);
        event2.setStatus(EventStatus.INACTIVE);
        event2.setStartTime(LocalDateTime.now(clock).plusDays(2));

        List<SportEvent> events = Arrays.asList(event1, event2);
        when(sportEventRepository.findAll()).thenReturn(events);

        // Act
        List<SportEventDto> result = sportEventService.getSportEvents(null, null);

        // Assert
        assertEquals(2, result.size());
        verify(sportEventRepository, times(1)).findAll();
    }

    @Test
    void getSportEvents_InvalidStatus_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> sportEventService.getSportEvents("INVALID_STATUS", null));
    }

    @Test
    void getSportEventById_EventExists_ReturnsEvent() {
        // Arrange
        Long eventId = 1L;
        SportType sportType = new SportType();
        sportType.setId(1L);
        sportType.setName("FOOTBALL");

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(eventId);
        sportEvent.setName("Championship Match");
        sportEvent.setSportType(sportType);
        sportEvent.setStatus(EventStatus.ACTIVE);
        sportEvent.setStartTime(LocalDateTime.now(clock).plusDays(1));

        when(sportEventRepository.findById(eventId)).thenReturn(Optional.of(sportEvent));

        // Act
        SportEventDto result = sportEventService.getSportEventById(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(eventId, result.getId());
        verify(sportEventRepository, times(1)).findById(eventId);
    }


    @Test
    void getSportEventById_EventNotFound_ThrowsException() {
        // Arrange
        Long eventId = 1L;

        when(sportEventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SportEventNotFoundException.class, () -> sportEventService.getSportEventById(eventId));
        verify(sportEventRepository, times(1)).findById(eventId);
    }
}