package com.example.sportevents.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusChangeRequest {

    @NotBlank
    private String status;
}