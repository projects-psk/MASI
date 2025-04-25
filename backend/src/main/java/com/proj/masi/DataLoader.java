package com.proj.masi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.masi.dto.structure.ParallelDto;
import com.proj.masi.dto.structure.SequenceDto;
import com.proj.masi.dto.structure.UnitermDto;
import com.proj.masi.model.UnitermDef;
import com.proj.masi.repository.UnitermDefRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final UnitermDefRepository repo;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (repo.findByName("SequenceAB").isEmpty()) {
            UnitermDto leafA = new UnitermDto("A");
            UnitermDto leafB = new UnitermDto("B");
            SequenceDto seqAB = new SequenceDto(
                    List.of(leafA, leafB),
                    ";"
            );

            JsonNode propsSeq = objectMapper.readTree("""
              {
                "fontSize": 12,
                "fontFamily": "Arial",
                "switched": false
              }
            """);

            UnitermDef seqExample = UnitermDef.builder()
                    .name("SequenceAB")
                    .description("Przykład sekwencjonowania A;B")
                    .drawingProps(propsSeq)
                    .structure(seqAB)
                    .build();

            repo.save(seqExample);
            log.info("✔️ Wstawiono SequenceAB = (A;B)");
        }

        if (repo.findByName("ParallelXZ").isEmpty()) {
            UnitermDto leafX = new UnitermDto("X");
            UnitermDto leafZ = new UnitermDto("Z");
            ParallelDto parXZ = new ParallelDto(
                    List.of(leafX, leafZ),
                    ","
            );

            JsonNode propsPar = objectMapper.readTree("""
              {
                "fontSize": 12,
                "fontFamily": "Arial",
                "switched": false
              }
            """);

            UnitermDef parExample = UnitermDef.builder()
                    .name("ParallelXZ")
                    .description("Przykład zrównoleglenia [X,Z]")
                    .drawingProps(propsPar)
                    .structure(parXZ)
                    .build();

            repo.save(parExample);
            log.info("✔️ Wstawiono ParallelXZ = [X,Z]");
        }

        log.info("📋 Wszystkie definicje unitermów w bazie:");
        repo.findAll().forEach(u -> {
            try {
                String astJson = objectMapper.writeValueAsString(u.getStructure());
                log.info(" • {} → {}", u.getName(), astJson);
            } catch (Exception e) {
                log.error("Błąd serializacji AST dla {}", u.getName(), e);
            }
        });
    }
}
