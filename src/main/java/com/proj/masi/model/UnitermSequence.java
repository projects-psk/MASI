package com.proj.masi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="uniterm_sequence",
        uniqueConstraints=@UniqueConstraint(columnNames = {"uniterm_id","position"}))
public class UnitermSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne @JoinColumn(name="uniterm_id", nullable=false)
    private UnitermDef uniterm;

    @Column(nullable=false)
    private Integer position;

    @Column(nullable=false)
    private String text;

    public UnitermSequence() {}

    public UnitermSequence(Integer position, String text, UnitermDef uniterm) {
        this.position = position;
        this.text     = text;
        this.uniterm  = uniterm;
    }

    @Override
    public String toString() {
        return "Seq{" + position + ":\"" + text + "\"}";
    }
}
