package com.proj.masi.dto.response;

import com.proj.masi.dto.structure.TermDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UnitermDefDto(
        UUID id,

        @NotBlank(message = "Name must not be blank")
        String name,

        String description,

        @NotNull(message = "Structure must be provided")
        TermDto structure

) {}
