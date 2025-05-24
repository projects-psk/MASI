package com.proj.gui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proj.masi.dto.request.SaveCustomRequest;
import com.proj.masi.dto.request.SaveTransformRequest;
import com.proj.masi.dto.request.TransformRequest;
import com.proj.masi.dto.response.TransformResultDto;
import com.proj.masi.dto.response.UnitermDefDto;
import com.proj.masi.dto.structure.TermDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class UnitermHttpClient {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON   = "application/json";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final ResourceBundle CONF =
            ResourceBundle.getBundle("application", Locale.getDefault());
    private static final String BASE_URL = CONF.getString("base.url");

    public List<UnitermDefDto> findAll() throws IOException, InterruptedException {
        var req  = HttpRequest.newBuilder(URI.create(BASE_URL)).GET().build();
        var body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, new TypeReference<>() {
        });
    }

    public UnitermDefDto create(UnitermDefDto toCreate) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(toCreate);
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL))
                .header(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, UnitermDefDto.class);
    }

    public TermDto transform(TransformRequest reqDto) throws IOException, InterruptedException {
        var json = mapper.writeValueAsString(reqDto);
        var req  = HttpRequest.newBuilder(URI.create(BASE_URL +"/transform"))
                .header(HEADER_CONTENT_TYPE,APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        var body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, TermDto.class);
    }

    public UnitermDefDto saveCustom(SaveCustomRequest reqDto) throws IOException, InterruptedException {
        var json = mapper.writeValueAsString(reqDto);
        var req  = HttpRequest.newBuilder(URI.create(BASE_URL +"/save"))
                .header(HEADER_CONTENT_TYPE,APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        var body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, UnitermDefDto.class);
    }

    public void delete(UUID id) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(BASE_URL +"/"+id)).DELETE().build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public void deleteTransformResult(UUID id) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(BASE_URL + "/transform/" + id))
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public ObjectNode emptyProps() {
        return mapper.createObjectNode();
    }

    public TransformResultDto saveTransform(
            SaveTransformRequest reqDto
    ) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(reqDto);
        HttpRequest req = HttpRequest.newBuilder(
                        URI.create(BASE_URL + "/transform/save"))
                .header(HEADER_CONTENT_TYPE,APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, TransformResultDto.class);
    }

    public List<TransformResultDto> findAllTransformResults() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(
                        URI.create(BASE_URL + "/transform/results"))
                .GET()
                .build();
        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body,
                new TypeReference<List<TransformResultDto>>() {});
    }
}
