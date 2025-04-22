package com.proj.masi.mapper;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.model.UnitermDef;
import com.proj.masi.model.UnitermExpansion;
import com.proj.masi.model.UnitermSequence;

import java.util.List;

public class UnitermDefMapper {

    private UnitermDefMapper() {
        throw new AssertionError("Cannot instantiate UnitermDefMapper");
    }

    public static UnitermDefDto toDto(UnitermDef entity) {
        return new UnitermDefDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDrawingProps(),
                entity.getSequence().stream()
                        .map(UnitermSequence::getText)
                        .toList(),
                entity.getExpansion().stream()
                        .map(UnitermExpansion::getText)
                        .toList()
        );
    }

    public static UnitermDef toEntity(UnitermDefDto dto) {
        var e = new UnitermDef();
        e.setName(dto.name());
        e.setDescription(dto.description());
        e.setDrawingProps(dto.drawingProps());
        return e;
    }

    public static void updateEntity(UnitermDef entity, UnitermDefDto dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setDrawingProps(dto.drawingProps());

        entity.getSequence().clear();
        List<UnitermSequence> seq = dto.sequence().stream()
                .map(text -> {
                    int pos = entity.getSequence().size() + 1;
                    return UnitermSequence.builder()
                            .position(pos)
                            .text(text)
                            .uniterm(entity)
                            .build();
                })
                .toList();
        entity.getSequence().addAll(seq);

        entity.getExpansion().clear();
        List<UnitermExpansion> exp = dto.expansion().stream()
                .map(text -> {
                    int pos = entity.getExpansion().size() + 1;
                    return UnitermExpansion.builder()
                            .position(pos)
                            .text(text)
                            .uniterm(entity)
                            .build();
                })
                .toList();
        entity.getExpansion().addAll(exp);
    }
}