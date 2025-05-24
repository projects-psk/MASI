package com.proj.masi.dto.request;

import com.proj.masi.dto.structure.TermDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record SaveCustomRequest(
        @NotBlank(message = "Name must not be blank")
        String name,

        String description,

        @NotNull(message = "Structure must be provided")
        TermDto structure
) {
    public SaveCustomRequest {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(structure,   "structure must not be null");
    }
}
