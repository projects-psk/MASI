package com.proj.masi.controller;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.service.UnitermService;
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
    public ResponseEntity<UnitermDefDto> create(@RequestBody UnitermDefDto dto) {
        UnitermDefDto created = service.create(dto);
        URI location = URI.create("/api/uniterms/" + created.id());
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitermDefDto> update(
            @PathVariable UUID id,
            @RequestBody UnitermDefDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
