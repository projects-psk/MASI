package com.proj.masi.dto;

import com.proj.masi.dto.structure.TermDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveTransformRequest(
        @NotBlank(message = "Name must not be blank")
        String name,
        String description,
        @NotNull(message = "Structure must be provided")
        TermDto structure
) {}