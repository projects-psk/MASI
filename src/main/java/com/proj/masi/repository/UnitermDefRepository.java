package com.proj.masi.repository;

import com.proj.masi.model.UnitermDef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UnitermDefRepository extends JpaRepository<UnitermDef,Integer> {
    Optional<UnitermDef> findByName(String name);
}
