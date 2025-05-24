package com.proj.masi.dto.response;

import com.proj.masi.dto.structure.TermDto;
import java.util.UUID;

public record TransformResultDto(
        UUID id,
        String name,
        String description,
        TermDto structure
) {}