package com.proj.masi.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record UnitermDefDto(
        UUID id,

        @NotBlank(message = "Name must not be blank")
        String name,

        String description,

        @NotNull(message = "Drawing properties must be provided")
        JsonNode drawingProps,

        @NotNull @Size(min = 1, message = "Sequence must contain at least one step")
        List<@NotBlank String> sequence,

        @NotNull @Size(min = 1, message = "Expansion must contain at least one step")
        List<@NotBlank String> expansion
) {}