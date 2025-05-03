package com.proj.masi.dto.structure;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UnitermDto.class,   name = "uniterm"),
        @JsonSubTypes.Type(value = SequenceDto.class,  name = "sequence"),
        @JsonSubTypes.Type(value = ParallelDto.class,  name = "parallel"),
        @JsonSubTypes.Type(value = CustomDto.class,    name = "custom")
})
public interface TermDto { }
