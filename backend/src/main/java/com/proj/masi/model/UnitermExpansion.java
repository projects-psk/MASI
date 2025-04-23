package com.proj.masi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="uniterm_expansion",
        uniqueConstraints=@UniqueConstraint(columnNames = {"uniterm_id","position"}))
public class UnitermExpansion {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uniterm_id", nullable = false)
    private UnitermDef uniterm;

    @Column(nullable=false)
    private Integer position;

    @Column(nullable=false)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitermExpansion)) return false;
        return id != null && id.equals(((UnitermExpansion) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

