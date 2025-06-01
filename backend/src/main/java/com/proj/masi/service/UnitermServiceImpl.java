package com.proj.masi.service;

import com.proj.masi.dto.request.SaveCustomRequest;
import com.proj.masi.dto.request.SaveTransformRequest;
import com.proj.masi.dto.request.TransformRequest;
import com.proj.masi.dto.response.TransformResultDto;
import com.proj.masi.dto.response.UnitermDefDto;
import com.proj.masi.dto.structure.CustomDto;
import com.proj.masi.dto.structure.TermDto;
import com.proj.masi.mapper.UnitermDefMapper;
import com.proj.masi.model.TransformResult;
import com.proj.masi.model.UnitermDef;
import com.proj.masi.repository.TransformResultRepository;
import com.proj.masi.repository.UnitermDefRepository;
import com.proj.masi.util.ASTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitermServiceImpl implements UnitermService {

    private static final String ENTITY = "UnitermDef";
    private final UnitermDefRepository repo;
    private final TransformResultRepository resultRepo;

    @Override
    public List<UnitermDefDto> findAll() {
        return repo.findAll().stream()
                .map(UnitermDefMapper::toDto)
                .toList();
    }

    @Override
    public UnitermDefDto findById(UUID id) {
        var e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
        return UnitermDefMapper.toDto(e);
    }

    @Override
    @Transactional
    public UnitermDefDto create(UnitermDefDto dto) {
        var ent = UnitermDefMapper.toEntity(dto);
        UnitermDefMapper.updateEntity(ent, dto);
        return UnitermDefMapper.toDto(repo.save(ent));
    }

    @Override
    @Transactional
    public UnitermDefDto update(UUID id, UnitermDefDto dto) {
        var existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
        UnitermDefMapper.updateEntity(existing, dto);
        return UnitermDefMapper.toDto(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        var existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
        repo.delete(existing);
    }

    @Override
    public TermDto transform(TransformRequest req) {
        var base = repo.findById(req.baseId())
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, req.baseId()));
        var repl = repo.findById(req.replacementId())
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, req.replacementId()));
        var newAst = ASTUtils.replaceAt(base.getStructure(), req.path(), repl.getStructure());
        return new CustomDto(newAst);
    }

    @Override
    @Transactional
    public UnitermDefDto saveCustom(SaveCustomRequest req) {
        UnitermDef def = UnitermDef.builder()
                .name(req.name())
                .description(req.description())
                .structure(req.structure())
                .build();
        var saved = repo.save(def);
        return UnitermDefMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TransformResultDto saveTransform(SaveTransformRequest req) {
        UnitermDef def = repo.findById(req.unitermDefinitionId())
                .orElseThrow(() -> new ResourceNotFoundException("UnitermDef", req.unitermDefinitionId()));
        TransformResult entity = TransformResult.builder()
                .unitermDef(def)
                .name(req.name())
                .description(req.description())
                .structure(req.structure())
                .build();

        TransformResult saved = resultRepo.save(entity);
        return new TransformResultDto(
                saved.getId(),
                def.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getStructure(),
                saved.getCreatedAt()
        );
    }

    @Override
    public List<TransformResultDto> findAllTransformResults() {
        return resultRepo.findAll().stream()
                .map(r -> new TransformResultDto(
                        r.getId(),
                        r.getUnitermDef().getId(),
                        r.getName(),
                        r.getDescription(),
                        r.getStructure(),
                        r.getCreatedAt()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void deleteResult(UUID id) {
        var existing = resultRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransformResult", id));
        resultRepo.delete(existing);
    }
}
