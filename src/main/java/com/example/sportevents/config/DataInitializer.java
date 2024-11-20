package com.example.sportevents.config;

import com.example.sportevents.model.SportType;
import com.example.sportevents.repository.SportTypeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataInitializer {

    private final SportTypeRepository sportTypeRepository;

    public DataInitializer(SportTypeRepository sportTypeRepository) {
        this.sportTypeRepository = sportTypeRepository;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        //TODO migrate to Flyway or Liquibase
        if (sportTypeRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("sport-types.json")) {
                if (is == null) {
                    throw new IllegalStateException("sport-types.json not found in resources");
                }
                List<SportType> sportTypes = mapper.readValue(is, new TypeReference<List<SportType>>() {
                });
                sportTypeRepository.saveAll(sportTypes);
            }
        }
    }
}