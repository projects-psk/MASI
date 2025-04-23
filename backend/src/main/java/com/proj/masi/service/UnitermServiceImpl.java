package com.proj.masi.service;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.mapper.UnitermDefMapper;
import com.proj.masi.model.UnitermDef;
import com.proj.masi.repository.UnitermDefRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitermServiceImpl implements UnitermService {

    private static final String ENTITY_NAME = "UnitermDef";

    private final UnitermDefRepository repo;

    @Override
    public List<UnitermDefDto> findAll() {
        return repo.findAll()
                .stream()
                .map(UnitermDefMapper::toDto)
                .toList();
    }

    @Override
    public UnitermDefDto findById(UUID id) {
        UnitermDef entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        return UnitermDefMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UnitermDefDto create(UnitermDefDto dto) {
        UnitermDef entity = UnitermDefMapper.toEntity(dto);
        UnitermDefMapper.updateEntity(entity, dto);
        UnitermDef saved = repo.save(entity);
        return UnitermDefMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UnitermDefDto update(UUID id, UnitermDefDto dto) {
        UnitermDef existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        UnitermDefMapper.updateEntity(existing, dto);
        return UnitermDefMapper.toDto(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        UnitermDef existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, id));
        repo.delete(existing);
    }
}
