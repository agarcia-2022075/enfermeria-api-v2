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

    // Inyectamos TODOS los repositorios necesarios a través del constructor
    public AtencionController(AtencionRepository atencionRepo,
                              PacienteRepository pacienteRepo,
                              PersonalRepository personalRepo,
                              InsumoRepository insumoRepo) {
        this.atencionRepo = atencionRepo;
        this.pacienteRepo = pacienteRepo;
        this.personalRepo = personalRepo;
        this.insumoRepo = insumoRepo;
    }

    // 1. LISTAR TODAS LAS ATENCIONES
    @GetMapping
    public List<Atencion> listar() {
        return atencionRepo.findAll();
    }

    // 2. GUARDAR ATENCIÓN (Magia Relacional usando Map para recibir IDs)
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Map<String, Object> datos) {
        try {
            Atencion nuevaAtencion = new Atencion();

            // Paso 1: Llenar datos básicos (haciendo conversiones seguras desde el JSON)
            nuevaAtencion.setMotivo((String) datos.get("motivo"));
            nuevaAtencion.setDiagnostico((String) datos.get("diagnostico"));
            nuevaAtencion.setTratamiento((String) datos.get("tratamiento"));

            // Convertir la fecha de String (ej. "2023-10-28") a LocalDate de Java
            if (datos.get("fecha") != null) {
                nuevaAtencion.setFecha(LocalDate.parse((String) datos.get("fecha")));
            }

            // Paso 2: BUSCAR Y ASIGNAR EL PACIENTE (Relación ManyToOne)
            Long pacienteId = Long.valueOf(datos.get("pacienteId").toString());
            Paciente p = pacienteRepo.findById(pacienteId)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + pacienteId));
            nuevaAtencion.setPaciente(p);

            // Paso 3: BUSCAR Y ASIGNAR EL ENFERMERO (Relación ManyToOne)
            Long enfermeroId = Long.valueOf(datos.get("enfermeroId").toString());
            Personal per = personalRepo.findById(enfermeroId)
                    .orElseThrow(() -> new RuntimeException("Personal no encontrado con el ID: " + enfermeroId));
            nuevaAtencion.setPersonal(per);

            // Paso 4: BUSCAR Y ASIGNAR INSUMOS (Relación ManyToMany - Tabla Intermedia)
            if (datos.get("insumoIds") != null) {
                // Obtenemos la lista de IDs de insumos que mandamos desde Postman
                List<Integer> idsRaw = (List<Integer>) datos.get("insumoIds");

                // Convertimos esos Integer a Long para que JPA los entienda
                List<Long> ids = idsRaw.stream().map(Integer::longValue).toList();

                // Buscamos todos esos insumos de golpe en la base de datos
                List<Insumo> listaInsumos = insumoRepo.findAllById(ids);
                nuevaAtencion.setInsumos(listaInsumos);
            }

            // Paso 5: Guardar todo el objeto armado en la base de datos
            return ResponseEntity.ok(atencionRepo.save(nuevaAtencion));

        } catch (Exception e) {
            // Si algo falla (ej. paciente no existe), devolvemos un error 400 Bad Request
            return ResponseEntity.badRequest().body("Error al guardar: " + e.getMessage());
        }
    }
}