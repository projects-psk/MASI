package com.proj.masi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.service.UnitermService;
import com.proj.masi.service.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UnitermController.class)
public class UnitermControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UnitermService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_returnsList() throws Exception {
        UnitermDefDto dto = new UnitermDefDto(
                UUID.randomUUID(),
                "A",
                "desc",
                null,
                List.of("s"),
                List.of("e")
        );
        Mockito.when(service.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/uniterms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("A"))
                .andExpect(jsonPath("$[0].sequence[0]").value("s"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(service.findById(id))
                .thenThrow(new ResourceNotFoundException("UnitermDef", id));

        mockMvc.perform(get("/api/uniterms/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("UnitermDef not found with id = " + id));
    }

    @Test
    void create_valid_returns201AndLocation() throws Exception {
        UnitermDefDto req = new UnitermDefDto(
                null,
                "C",
                "descC",
                null,
                List.of("s"),
                List.of("e")
        );
        UnitermDefDto res = new UnitermDefDto(
                UUID.randomUUID(),
                "C",
                "descC",
                null,
                List.of("s"),
                List.of("e")
        );
        Mockito.when(service.create(any())).thenReturn(res);

        mockMvc.perform(post("/api/uniterms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/uniterms/" + res.id()))
                .andExpect(jsonPath("$.id").value(res.id().toString()));
    }

    @Test
    void update_valid_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        UnitermDefDto req = new UnitermDefDto(
                null,
                "D",
                "descD",
                null,
                List.of("s"),
                List.of("e")
        );
        UnitermDefDto res = new UnitermDefDto(
                id,
                "D",
                "descD",
                null,
                List.of("s"),
                List.of("e")
        );
        Mockito.when(service.update(eq(id), any())).thenReturn(res);

        mockMvc.perform(put("/api/uniterms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void delete_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/uniterms/{id}", id))
                .andExpect(status().isNoContent());
    }
}

