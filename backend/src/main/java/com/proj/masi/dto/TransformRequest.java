package com.proj.masi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record TransformRequest(
        @NotNull UUID baseId,
        @NotNull UUID replacementId,
        @NotEmpty List<@NotNull Integer> path
) {
    public TransformRequest {
        Objects.requireNonNull(baseId,       "baseId must not be null");
        Objects.requireNonNull(replacementId,"replacementId must not be null");
        Objects.requireNonNull(path,         "path must not be null");
        if (path.isEmpty()) {
            throw new IllegalArgumentException("path must contain at least one index");
        }
    }
}
