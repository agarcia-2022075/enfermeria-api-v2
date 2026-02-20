package com.enfermeria.enfermeria_app.repository;

import com.enfermeria.enfermeria_app.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}