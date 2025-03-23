package com.muxegresso.egresso.domain.dtos;

import jakarta.validation.constraints.NotBlank;

// Dto para receber no request de login do front
public record AutenticationDataDto(
        @NotBlank
        String data,
        @NotBlank
        String password,
        @NotBlank
        String role){
}
