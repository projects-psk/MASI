package com.proj.masi.controller;

import com.proj.masi.dto.UnitermDefDto;
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
    public ResponseEntity<UnitermDefDto> create(@Valid @RequestBody UnitermDefDto dto) {
        UnitermDefDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/uniterms/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public UnitermDefDto update(
            @PathVariable UUID id,
            @Valid @RequestBody UnitermDefDto dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
