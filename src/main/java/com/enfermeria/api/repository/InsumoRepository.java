package com.enfermeria.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.enfermeria.api.entity.Insumo;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}