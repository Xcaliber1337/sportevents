package com.example.sportevents.config;

import com.example.sportevents.repository.SportTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class DataInitializerTest {

    @InjectMocks
    private DataInitializer dataInitializer;

    @Mock
    private SportTypeRepository sportTypeRepository;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void init_RepositoryNotEmpty_DoesNotLoadData() {
        // Arrange
        when(sportTypeRepository.count()).thenReturn(10L);

        // Act
        dataInitializer.init();

        // Assert
        verify(sportTypeRepository, never()).saveAll(anyList());
    }

}
