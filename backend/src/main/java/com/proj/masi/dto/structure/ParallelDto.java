package com.proj.masi.dto.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public record ParallelDto(
        List<TermDto> children,
        @JsonProperty(defaultValue = ";") String separator
) implements TermDto {

    public ParallelDto {
        Objects.requireNonNull(children, "children must not be null");
        Objects.requireNonNull(separator, "separator must not be null");
        if (children.size() < 2) {
            throw new IllegalArgumentException("parallel must have at least 2 children");
        }
        if (separator.isEmpty()) {
            throw new IllegalArgumentException("separator must not be empty");
        }
    }
}
