package com.example.sportevents.exception;

import com.example.sportevents.controller.SportEventController;
import com.example.sportevents.service.SportEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SportEventController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportEventService sportEventService;

    @Test
    void handleInvalidSportTypeException_ReturnsBadRequest() throws Exception {
        // Arrange
        doThrow(new InvalidSportTypeException("Invalid sport type"))
                .when(sportEventService).getSportEventById(1L);

        // Act & Assert
        mockMvc.perform(get("/api/sport-events/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid sport type"));
    }

    @Test
    void handleSportEventNotFoundException_ReturnsNotFound() throws Exception {
        // Arrange
        doThrow(new SportEventNotFoundException("Sport event not found"))
                .when(sportEventService).getSportEventById(1L);

        // Act & Assert
        mockMvc.perform(get("/api/sport-events/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Sport event not found"));
    }

    @Test
    void handleInvalidStatusTransitionException_ReturnsBadRequest() throws Exception {
        // Arrange
        doThrow(new InvalidStatusTransitionException("Invalid status transition"))
                .when(sportEventService).changeSportEventStatus(1L, "FINISHED");

        // Act & Assert
        mockMvc.perform(put("/api/sport-events/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"FINISHED\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid status transition"));
    }

    @Test
    void handleInvalidStatusException_ReturnsBadRequest() throws Exception {
        // Arrange
        doThrow(new InvalidStatusException("Invalid status provided"))
                .when(sportEventService).getSportEvents("INVALID_STATUS", null);

        // Act & Assert
        mockMvc.perform(get("/api/sport-events?status=INVALID_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid status provided"));
    }

    @Test
    void handleGeneralException_ReturnsInternalServerError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Unexpected error"))
                .when(sportEventService).getSportEventById(1L);

        // Act & Assert
        mockMvc.perform(get("/api/sport-events/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred."));
    }
}