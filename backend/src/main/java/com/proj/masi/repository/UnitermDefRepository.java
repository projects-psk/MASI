package com.proj.masi.repository;

import com.proj.masi.model.UnitermDef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UnitermDefRepository extends JpaRepository<UnitermDef, UUID> {
    Optional<UnitermDef> findByName(String name);
}
