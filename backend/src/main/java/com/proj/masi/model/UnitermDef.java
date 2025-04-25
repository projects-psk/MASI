package com.proj.masi.model;

import com.proj.masi.dto.structure.TermDto;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "uniterm_definitions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UnitermDef {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1024)
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "drawing_props", columnDefinition = "jsonb")
    private JsonNode drawingProps;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "structure", columnDefinition = "jsonb")
    private TermDto structure;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitermDef that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
