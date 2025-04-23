package com.proj.masi.service;

import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.model.UnitermDef;
import com.proj.masi.repository.UnitermDefRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UnitermServiceImplTest {
    @Mock
    private UnitermDefRepository repo;
    private UnitermServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UnitermServiceImpl(repo);
    }

    @Test
    void findAll_returnsDtos() {
        UnitermDef e = new UnitermDef();
        e.setId(UUID.randomUUID());
        e.setName("A");
        e.setDescription("desc");
        e.setDrawingProps(null);
        when(repo.findAll()).thenReturn(List.of(e));

        List<UnitermDefDto> dtos = service.findAll();

        assertEquals(1, dtos.size());
        assertEquals("A", dtos.get(0).name());
    }

    @Test
    void findById_found() {
        UUID id = UUID.randomUUID();
        UnitermDef e = new UnitermDef();
        e.setId(id);
        e.setName("B");
        e.setDescription("descB");
        e.setDrawingProps(null);
        when(repo.findById(id)).thenReturn(Optional.of(e));

        UnitermDefDto dto = service.findById(id);

        assertEquals(id, dto.id());
        assertEquals("B", dto.name());
    }

    @Test
    void findById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void create_savesAndReturnsDto() {
        UnitermDefDto dto = new UnitermDefDto(
                null,
                "C",
                "descC",
                null,
                List.of("s1"),
                List.of("e1")
        );
        when(repo.save(any(UnitermDef.class))).thenAnswer(invocation -> {
            UnitermDef arg = invocation.getArgument(0);
            arg.setId(UUID.randomUUID());
            return arg;
        });

        UnitermDefDto result = service.create(dto);

        assertNotNull(result.id());
        assertEquals("C", result.name());
        assertEquals(List.of("s1"), result.sequence());
        assertEquals(List.of("e1"), result.expansion());
    }

    @Test
    void update_existing_updatesFields() {
        UUID id = UUID.randomUUID();
        UnitermDef existing = new UnitermDef();
        existing.setId(id);
        existing.setSequence(new java.util.ArrayList<>());
        existing.setExpansion(new java.util.ArrayList<>());
        when(repo.findById(id)).thenReturn(Optional.of(existing));

        UnitermDefDto dto = new UnitermDefDto(
                id,
                "new",
                "newDesc",
                null,
                List.of("snew"),
                List.of("enew")
        );

        UnitermDefDto result = service.update(id, dto);

        assertEquals(id, result.id());
        assertEquals("new", result.name());
        assertEquals(List.of("snew"), result.sequence());
        assertEquals(List.of("enew"), result.expansion());
    }

    @Test
    void delete_existing_deletes() {
        UUID id = UUID.randomUUID();
        UnitermDef existing = new UnitermDef();
        existing.setId(id);
        when(repo.findById(id)).thenReturn(Optional.of(existing));

        service.delete(id);

        verify(repo).delete(existing);
    }

    @Test
    void delete_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
    }
}

