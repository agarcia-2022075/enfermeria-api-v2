package com.enfermeria.api.repository;

import com.enfermeria.enfermeria_app.entity.Atencion;
import com.enfermeria.enfermeria_app.entity.Paciente;
import com.enfermeria.enfermeria_app.entity.PersonalEnfermeria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    // Buscar atenciones por objeto Paciente completo
    List<Atencion> findByPaciente(Paciente paciente);

    // Buscar atenciones por ID de paciente (versión con @Query)
    @Query("SELECT a FROM Atencion a WHERE a.paciente.id = :pacienteId")
    List<Atencion> findByPacienteId(@Param("pacienteId") Long pacienteId);

    // Buscar atenciones por objeto PersonalEnfermeria completo
    List<Atencion> findByPersonal(PersonalEnfermeria personal);

    // Buscar atenciones por ID de personal (versión con @Query)
    @Query("SELECT a FROM Atencion a WHERE a.personal.id = :personalId")
    List<Atencion> findByPersonalId(@Param("personalId") Long personalId);

    // Buscar atenciones por fecha específica
    List<Atencion> findByFecha(LocalDate fecha);

    // Buscar atenciones entre fechas
    List<Atencion> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Contar atenciones por paciente
    Long countByPacienteId(Long pacienteId);
}