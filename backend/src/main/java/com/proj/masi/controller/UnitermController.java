package com.proj.masi.controller;

import com.proj.masi.dto.request.SaveCustomRequest;
import com.proj.masi.dto.request.SaveTransformRequest;
import com.proj.masi.dto.request.TransformRequest;
import com.proj.masi.dto.response.TransformResultDto;
import com.proj.masi.dto.response.UnitermDefDto;
import com.proj.masi.dto.structure.TermDto;
import com.proj.masi.service.UnitermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/uniterms")
@RequiredArgsConstructor
public class UnitermController {

    private final UnitermService service;

    @GetMapping
    public List<UnitermDefDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UnitermDefDto getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<UnitermDefDto> create(
            @Valid @RequestBody UnitermDefDto dto
    ) {
        UnitermDefDto created = service.create(dto);
        URI location = URI.create("/api/uniterms/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitermDefDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UnitermDefDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @DeleteMapping("/transform/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransformResult(@PathVariable UUID id) {
        service.deleteResult(id);
    }

    @PostMapping("/transform")
    public ResponseEntity<TermDto> transform(
            @Valid @RequestBody TransformRequest req
    ) {
        TermDto result = service.transform(req);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/save")
    public ResponseEntity<UnitermDefDto> saveCustom(
            @Valid @RequestBody SaveCustomRequest req
    ) {
        var saved = service.saveCustom(req);
        URI location = URI.create("/api/uniterms/" + saved.id());
        return ResponseEntity.created(location).body(saved);
    }

    @PostMapping("/transform/save")
    public ResponseEntity<TransformResultDto> saveTransform(
        @Valid @RequestBody SaveTransformRequest req
    ) {
        TransformResultDto dto = service.saveTransform(req);
        URI location = URI.create("/api/uniterms/transform/save/" + dto.id());
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/transform/results")
    public List<TransformResultDto> getAllTransformResults() {
        return service.findAllTransformResults();
    }
}
