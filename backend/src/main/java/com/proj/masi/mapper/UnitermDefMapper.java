package com.proj.masi.mapper;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.model.UnitermDef;

public class UnitermDefMapper {

    private UnitermDefMapper() { }

    public static UnitermDefDto toDto(UnitermDef e) {
        return new UnitermDefDto(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getDrawingProps(),
                e.getStructure()
        );
    }

    public static UnitermDef toEntity(UnitermDefDto dto) {
        UnitermDef e = new UnitermDef();
        e.setName(dto.name());
        e.setDescription(dto.description());
        e.setDrawingProps(dto.drawingProps());
        e.setStructure(dto.structure());
        return e;
    }

    public static void updateEntity(UnitermDef e, UnitermDefDto dto) {
        e.setName(dto.name());
        e.setDescription(dto.description());
        e.setDrawingProps(dto.drawingProps());
        e.setStructure(dto.structure());
    }
}
