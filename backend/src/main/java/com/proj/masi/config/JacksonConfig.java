package com.proj.masi.config;

import com.proj.masi.dto.structure.CustomDto;
import com.proj.masi.dto.structure.ParallelDto;
import com.proj.masi.dto.structure.SequenceDto;
import com.proj.masi.dto.structure.UnitermDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.build();
        mapper.registerSubtypes(
                new NamedType(UnitermDto.class,   "uniterm"),
                new NamedType(SequenceDto.class,  "sequence"),
                new NamedType(ParallelDto.class,  "parallel"),
                new NamedType(CustomDto.class,    "custom")
        );
        return mapper;
    }
}
