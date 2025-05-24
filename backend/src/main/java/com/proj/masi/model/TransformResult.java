package com.proj.masi.model;

import com.proj.masi.dto.structure.TermDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Entity
@Table(name = "uniterm_transform_results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransformResult {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private TermDto structure;
}