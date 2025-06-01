package com.proj.masi.dto.response;

import com.proj.masi.dto.structure.TermDto;

import java.time.Instant;
import java.util.UUID;

public record TransformResultDto(
        UUID id,
        UUID unitermDefinitionId,
        String name,
        String description,
        TermDto structure,
        Instant createdAt
) {}