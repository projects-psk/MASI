package com.proj.masi.dto;

import com.proj.masi.dto.structure.TermDto;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UnitermDefDto(
        UUID id,

        @NotBlank(message = "Name must not be blank")
        String name,

        String description,

        @NotNull(message = "Drawing properties must be provided")
        JsonNode drawingProps,

        @NotNull(message = "Structure must be provided")
        TermDto structure

) {}
