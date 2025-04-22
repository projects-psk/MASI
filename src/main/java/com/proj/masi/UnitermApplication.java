package com.proj.masi;

import com.proj.masi.model.UnitermDef;
import com.proj.masi.model.UnitermSequence;
import com.proj.masi.repository.UnitermDefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class UnitermApplication {
    private static final Logger log = LoggerFactory.getLogger(UnitermApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UnitermApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadTestData(UnitermDefRepository repo) {
        return args -> {
            Optional<UnitermDef> existing = repo.findByName("Swap");
            if (existing.isEmpty()) {
                UnitermDef swap = new UnitermDef();
                swap.setName("Swap");
                swap.setDescription("Zamienia wartości x i y");

                swap.setDrawingProps("""
                  {"fontSize":14,"fontFamily":"Serif","switched":false}
                  """);

                swap.getSequence().add(new UnitermSequence(1, "temp := x", swap));
                swap.getSequence().add(new UnitermSequence(2, "x := y", swap));
                swap.getSequence().add(new UnitermSequence(3, "y := temp", swap));

                repo.save(swap);
                log.info("Wstawiono przykładowy uniterm Swap");
            } else {
                log.info("Uniterm Swap już był w bazie, pomijam wstawianie");
            }

            log.info("Lista wszystkich unitermów w bazie:");
            repo.findAll().forEach(u -> log.info("  {}", u));
        };
    }
}
