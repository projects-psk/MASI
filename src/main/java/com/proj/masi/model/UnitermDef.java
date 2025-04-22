package com.proj.masi.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="uniterm_definitions")
public class UnitermDef {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false, unique=true)
    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="drawing_props", columnDefinition="jsonb")
    private JsonNode drawingProps;

    @Builder.Default
    @OneToMany(mappedBy="uniterm", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    @OrderBy("position")
    private List<UnitermSequence> sequence = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy="uniterm", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    @OrderBy("position")
    private List<UnitermExpansion> expansion = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitermDef)) return false;
        return id != null && id.equals(((UnitermDef) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

