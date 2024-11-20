package com.example.sportevents.controller;

import com.example.sportevents.model.SportType;
import com.example.sportevents.service.SportTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SportTypeController.class)
class SportTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportTypeService sportTypeService;

    @Test
    void getAllSportTypes_ReturnsOk() throws Exception {
        // Arrange
        List<SportType> sportTypes = Arrays.asList(new SportType(), new SportType());
        when(sportTypeService.getAllSportTypes()).thenReturn(sportTypes);

        // Act & Assert
        mockMvc.perform(get("/api/sport-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}