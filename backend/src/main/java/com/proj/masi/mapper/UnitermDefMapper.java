package com.proj.masi.mapper;

import com.proj.masi.dto.response.UnitermDefDto;
import com.proj.masi.model.UnitermDef;

public class UnitermDefMapper {

    private UnitermDefMapper() { }

    public static UnitermDefDto toDto(UnitermDef e) {
        return new UnitermDefDto(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getStructure()
        );
    }

    public static UnitermDef toEntity(UnitermDefDto dto) {
        UnitermDef e = new UnitermDef();
        updateEntity(e, dto);
        return e;
    }

    public static void updateEntity(UnitermDef e, UnitermDefDto dto) {
        e.setName(dto.name());
        e.setDescription(dto.description());
        e.setStructure(dto.structure());
    }
}
