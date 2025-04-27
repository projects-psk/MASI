package com.proj.gui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.masi.dto.TransformRequest;
import com.proj.masi.dto.SaveCustomRequest;
import com.proj.masi.dto.UnitermDefDto;
import com.proj.masi.dto.structure.TermDto;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class UnitermHttpClient {
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

    public TermDto transform(TransformRequest reqDto) throws IOException, InterruptedException {
        var json = mapper.writeValueAsString(reqDto);
        var req  = HttpRequest.newBuilder(URI.create(BASE_URL +"/transform"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        var body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, TermDto.class);
    }

    public UnitermDefDto saveCustom(SaveCustomRequest reqDto) throws IOException, InterruptedException {
        var json = mapper.writeValueAsString(reqDto);
        var req  = HttpRequest.newBuilder(URI.create(BASE_URL +"/save"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        var body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readValue(body, UnitermDefDto.class);
    }

    public void delete(UUID id) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(URI.create(BASE_URL +"/"+id)).DELETE().build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public ObjectNode emptyProps() {
        return mapper.createObjectNode();
    }
}
