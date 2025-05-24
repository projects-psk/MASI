package com.proj.masi.repository;

import com.proj.masi.model.TransformResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransformResultRepository extends JpaRepository<TransformResult, UUID> { }