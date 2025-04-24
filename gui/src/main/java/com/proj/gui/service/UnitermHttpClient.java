package com.proj.gui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.masi.dto.UnitermDefDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.UUID;

public class UnitermHttpClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/api/uniterms";

    public List<UnitermDefDto> findAll() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(baseUrl)).GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), new TypeReference<>(){});
    }

    public UnitermDefDto findById(UUID id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(baseUrl + "/" + id)).GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), UnitermDefDto.class);
    }

    public UnitermDefDto create(UnitermDefDto dto) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(dto);
        HttpRequest req = HttpRequest.newBuilder(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), UnitermDefDto.class);
    }

    public UnitermDefDto update(UUID id, UnitermDefDto dto) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(dto);
        HttpRequest req = HttpRequest.newBuilder(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), UnitermDefDto.class);
    }

    public void delete(UUID id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(baseUrl + "/" + id))
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }
}
