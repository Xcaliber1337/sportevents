package com.example.sportevents.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SportEventDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Long sportTypeId;

    private String sportTypeName;

    private String status;

    @NotNull
    @Future
    private LocalDateTime startTime;
}