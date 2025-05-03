package com.proj.masi.dto.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public record SequenceDto(
        List<TermDto> children,
        @JsonProperty(defaultValue = ";") String separator
) implements TermDto {

    public SequenceDto {
        Objects.requireNonNull(children, "children must not be null");
        Objects.requireNonNull(separator, "separator must not be null");
        if (separator.isEmpty()) {
            throw new IllegalArgumentException("separator must not be empty");
        }
    }
}
