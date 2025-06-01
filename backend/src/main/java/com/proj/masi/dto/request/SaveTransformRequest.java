package com.proj.masi.dto.request;

import com.proj.masi.dto.structure.TermDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaveTransformRequest(
        @NotNull(message = "unitermDefinitionId must not be null")
        UUID unitermDefinitionId,
        @NotBlank(message = "Name must not be blank")
        String name,
        String description,
        @NotNull(message = "Structure must be provided")
        TermDto structure
) {}