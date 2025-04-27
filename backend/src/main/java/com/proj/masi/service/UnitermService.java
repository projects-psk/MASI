package com.proj.masi.service;

import com.proj.masi.dto.SaveCustomRequest;
import com.proj.masi.dto.TransformRequest;
import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.dto.structure.TermDto;

import java.util.List;
import java.util.UUID;

public interface UnitermService {
    List<UnitermDefDto> findAll();
    UnitermDefDto findById(UUID id);
    UnitermDefDto create(UnitermDefDto dto);
    UnitermDefDto update(UUID id, UnitermDefDto dto);
    void delete(UUID id);
    TermDto transform(TransformRequest req);
    UnitermDefDto saveCustom(SaveCustomRequest req);
}
