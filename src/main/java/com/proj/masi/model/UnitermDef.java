package com.proj.masi.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name="uniterm_definitions")
public class UnitermDef {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true)
    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="drawing_props", columnDefinition="jsonb")
    private JsonNode drawingProps;

    @OneToMany(mappedBy="uniterm", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("position")
    private List<UnitermSequence> sequence = new ArrayList<>();

    @OneToMany(mappedBy="uniterm", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("position")
    private List<UnitermExpansion> expansion = new ArrayList<>();

    public void setDrawingProps(String json) {
        try {
            this.drawingProps = new ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("Niepoprawny JSON dla drawingProps", e);
        }
    }
}

