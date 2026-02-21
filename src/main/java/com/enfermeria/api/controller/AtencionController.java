package com.enfermeria.api.controller;

import com.enfermeria.api.entity.Atencion;
import com.enfermeria.api.entity.Paciente;
import com.enfermeria.api.entity.Personal;
import com.enfermeria.api.entity.Insumo;
import com.enfermeria.api.repository.AtencionRepository;
import com.enfermeria.api.repository.PacienteRepository;
import com.enfermeria.api.repository.PersonalRepository;
import com.enfermeria.api.repository.InsumoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/atenciones")
public class AtencionController {

    private final AtencionRepository atencionRepo;
    private final PacienteRepository pacienteRepo;
    private final PersonalRepository personalRepo;
    private final InsumoRepository insumoRepo;

    // Constructor con inyección de dependencias
    public AtencionController(AtencionRepository atencionRepo,
                              PacienteRepository pacienteRepo,
                              PersonalRepository personalRepo,
                              InsumoRepository insumoRepo) {
        this.atencionRepo = atencionRepo;
        this.pacienteRepo = pacienteRepo;
        this.personalRepo = personalRepo;
        this.insumoRepo = insumoRepo;
    }


    @GetMapping
    public List<Atencion> listar() {
        return atencionRepo.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Atencion> obtenerPorId(@PathVariable Long id) {
        return atencionRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Map<String, Object> datos) {
        try {
            Atencion nuevaAtencion = new Atencion();

            // Campos básicos
            nuevaAtencion.setMotivo((String) datos.get("motivo"));
            nuevaAtencion.setDiagnostico((String) datos.get("diagnostico"));
            nuevaAtencion.setTratamiento((String) datos.get("tratamiento"));

            if (datos.get("fecha") != null) {
                nuevaAtencion.setFecha(LocalDate.parse((String) datos.get("fecha")));
            }

            // Asignar paciente
            if (datos.containsKey("pacienteId")) {
                Long pacienteId = Long.valueOf(datos.get("pacienteId").toString());
                Paciente p = pacienteRepo.findById(pacienteId)
                        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pacienteId));
                nuevaAtencion.setPaciente(p);
            }

            // Asignar personal/enfermero
            if (datos.containsKey("enfermeroId")) {
                Long enfermeroId = Long.valueOf(datos.get("enfermeroId").toString());
                Personal per = personalRepo.findById(enfermeroId)
                        .orElseThrow(() -> new RuntimeException("Personal no encontrado con ID: " + enfermeroId));
                nuevaAtencion.setPersonal(per);
            }

            // Asignar insumos
            if (datos.containsKey("insumoIds") && datos.get("insumoIds") != null) {
                List<Integer> idsRaw = (List<Integer>) datos.get("insumoIds");
                List<Long> ids = idsRaw.stream().map(Integer::longValue).toList();
                List<Insumo> listaInsumos = insumoRepo.findAllById(ids);
                nuevaAtencion.setInsumos(listaInsumos);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(atencionRepo.save(nuevaAtencion));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        try {
            // Buscar la atención existente
            Atencion atencionExistente = atencionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Atención no encontrada con ID: " + id));

            // Actualizar campos básicos (solo si vienen en el JSON)
            if (datos.containsKey("motivo")) {
                atencionExistente.setMotivo((String) datos.get("motivo"));
            }
            if (datos.containsKey("diagnostico")) {
                atencionExistente.setDiagnostico((String) datos.get("diagnostico"));
            }
            if (datos.containsKey("tratamiento")) {
                atencionExistente.setTratamiento((String) datos.get("tratamiento"));
            }
            if (datos.containsKey("fecha")) {
                atencionExistente.setFecha(LocalDate.parse((String) datos.get("fecha")));
            }

            // Actualizar paciente
            if (datos.containsKey("pacienteId")) {
                Long pacienteId = Long.valueOf(datos.get("pacienteId").toString());
                Paciente p = pacienteRepo.findById(pacienteId)
                        .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pacienteId));
                atencionExistente.setPaciente(p);
            }

            // Actualizar personal/enfermero
            if (datos.containsKey("enfermeroId")) {
                Long enfermeroId = Long.valueOf(datos.get("enfermeroId").toString());
                Personal per = personalRepo.findById(enfermeroId)
                        .orElseThrow(() -> new RuntimeException("Personal no encontrado con ID: " + enfermeroId));
                atencionExistente.setPersonal(per);
            }

            // Actualizar insumos
            if (datos.containsKey("insumoIds") && datos.get("insumoIds") != null) {
                List<Integer> idsRaw = (List<Integer>) datos.get("insumoIds");
                List<Long> ids = idsRaw.stream().map(Integer::longValue).toList();
                List<Insumo> listaInsumos = insumoRepo.findAllById(ids);
                atencionExistente.setInsumos(listaInsumos);
            }

            // Guardar cambios
            return ResponseEntity.ok(atencionRepo.save(atencionExistente));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (atencionRepo.existsById(id)) {
            atencionRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/paciente/{pacienteId}")
    public List<Atencion> listarPorPaciente(@PathVariable Long pacienteId) {
        return atencionRepo.findByPacienteId(pacienteId);
    }


    @GetMapping("/personal/{personalId}")
    public List<Atencion> listarPorPersonal(@PathVariable Long personalId) {
        return atencionRepo.findByPersonalId(personalId);
    }


    @GetMapping("/fecha/{fecha}")
    public List<Atencion> listarPorFecha(@PathVariable String fecha) {
        LocalDate fechaBuscar = LocalDate.parse(fecha);
        return atencionRepo.findByFecha(fechaBuscar);
    }
}