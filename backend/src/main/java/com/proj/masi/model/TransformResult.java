package com.proj.masi.model;

import com.proj.masi.dto.structure.TermDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "uniterm_transform_results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransformResult {

    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uniterm_definition_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_transformresult_definition"))
    private UnitermDef unitermDef;

    @Column(nullable = false)
    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private TermDto structure;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}