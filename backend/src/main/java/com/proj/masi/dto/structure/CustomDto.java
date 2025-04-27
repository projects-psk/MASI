package com.proj.masi.dto.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public record CustomDto(
        @JsonProperty("root") TermDto root
) implements TermDto {
    @JsonCreator
    public CustomDto {
        Objects.requireNonNull(root, "root must not be null");
    }
}
