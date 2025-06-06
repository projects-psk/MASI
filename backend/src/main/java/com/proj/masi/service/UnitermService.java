package com.proj.masi.service;

import com.proj.masi.dto.request.SaveCustomRequest;
import com.proj.masi.dto.request.SaveTransformRequest;
import com.proj.masi.dto.request.TransformRequest;
import com.proj.masi.dto.response.TransformResultDto;
import com.proj.masi.dto.response.UnitermDefDto;
import com.proj.masi.dto.structure.TermDto;

import java.util.List;
import java.util.UUID;

public interface UnitermService {
    List<UnitermDefDto> findAll();
    UnitermDefDto findById(UUID id);
    UnitermDefDto create(UnitermDefDto dto);
    UnitermDefDto update(UUID id, UnitermDefDto dto);
    void delete(UUID id);
    void deleteResult(UUID id);
    TermDto transform(TransformRequest req);
    UnitermDefDto saveCustom(SaveCustomRequest req);
    TransformResultDto saveTransform(
            SaveTransformRequest req
    );
    List<TransformResultDto> findAllTransformResults();
}
