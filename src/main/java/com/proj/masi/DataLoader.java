package com.proj.masi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.masi.model.UnitermDef;
import com.proj.masi.model.UnitermExpansion;
import com.proj.masi.model.UnitermSequence;
import com.proj.masi.repository.UnitermDefRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
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
        var existing = repo.findByName("Swap");
        if (existing.isEmpty()) {
            JsonNode props = objectMapper.readTree("""
              {
                "fontSize": 14,
                "fontFamily": "Serif",
                "switched": false
              }
            """);

            var swap = UnitermDef.builder()
                    .name("Swap")
                    .description("Zamienia wartości x i y")
                    .drawingProps(props)
                    .build();

            swap.getSequence().addAll(List.of(
                    UnitermSequence.builder().position(1).text("temp := x").uniterm(swap).build(),
                    UnitermSequence.builder().position(2).text("x := y").uniterm(swap).build(),
                    UnitermSequence.builder().position(3).text("y := temp").uniterm(swap).build()
            ));

            swap.getExpansion().addAll(List.of(
                    UnitermExpansion.builder().position(1).text("temp := x").uniterm(swap).build(),
                    UnitermExpansion.builder().position(2).text("x := y").uniterm(swap).build(),
                    UnitermExpansion.builder().position(3).text("y := temp").uniterm(swap).build()
            ));

            repo.save(swap);
            log.info("Wstawiono przykładowy uniterm Swap");
        } else {
            log.info("Uniterm Swap już istnieje, pomijam wstawianie");
        }

        // Teraz mamy otwartą sesję i transakcję – kolekcje są init‑owane lazily bez wyjątku
        log.info("Lista wszystkich unitermów w bazie:");
        repo.findAll().forEach(u ->
                log.info("  {}: seq={} / exp={}",
                        u.getName(),
                        u.getSequence().size(),
                        u.getExpansion().size()
                )
        );
    }
}
