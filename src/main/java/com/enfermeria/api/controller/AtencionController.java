package com.enfermeria.api.controller;

import com.enfermeria.api.entity.Atencion;
import com.enfermeria.api.entity.Paciente;
import com.enfermeria.api.entity.Personal;
import com.enfermeria.api.entity.Insumo;
import com.enfermeria.api.repository.AtencionRepository;
import com.enfermeria.api.repository.PacienteRepository;
import com.enfermeria.api.repository.PersonalRepository;
import com.enfermeria.api.repository.InsumoRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/atenciones")
public class AtencionController {

    private final AtencionRepository atencionRepo;
    private final PacienteRepository pacienteRepo;
    private final PersonalRepository personalRepo;
    private final InsumoRepository insumoRepo;

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

    // 2. BUSCAR ATENCIÓN POR ID
    @GetMapping("/{id}")
    public Optional<Atencion> buscarPorId(@PathVariable Long id) {
        return atencionRepo.findById(id);
    }

    // 3. GUARDAR ATENCIÓN
    @PostMapping
    public Atencion guardar(@RequestBody Map<String, Object> datos) {
        Atencion nuevaAtencion = new Atencion();

        nuevaAtencion.setMotivo((String) datos.get("motivo"));
        nuevaAtencion.setDiagnostico((String) datos.get("diagnostico"));
        nuevaAtencion.setTratamiento((String) datos.get("tratamiento"));

        if (datos.get("fecha") != null) {
            nuevaAtencion.setFecha(LocalDate.parse((String) datos.get("fecha")));
        }

        Long pacienteId = Long.valueOf(datos.get("pacienteId").toString());
        nuevaAtencion.setPaciente(pacienteRepo.findById(pacienteId).get());

        Long enfermeroId = Long.valueOf(datos.get("enfermeroId").toString());
        nuevaAtencion.setPersonal(personalRepo.findById(enfermeroId).get());

        if (datos.get("insumoIds") != null) {
            List<Integer> idsRaw = (List<Integer>) datos.get("insumoIds");
            List<Long> ids = idsRaw.stream().map(Integer::longValue).toList();
            nuevaAtencion.setInsumos(insumoRepo.findAllById(ids));
        }

        return atencionRepo.save(nuevaAtencion);
    }

    // 4. ACTUALIZAR ATENCIÓN
    @PutMapping("/{id}")
    public Atencion actualizar(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        // Buscamos la atención existente y la sobreescribimos
        Atencion atencion = atencionRepo.findById(id).get();

        atencion.setMotivo((String) datos.get("motivo"));
        atencion.setDiagnostico((String) datos.get("diagnostico"));
        atencion.setTratamiento((String) datos.get("tratamiento"));

        if (datos.get("fecha") != null) {
            atencion.setFecha(LocalDate.parse((String) datos.get("fecha")));
        }

        Long pacienteId = Long.valueOf(datos.get("pacienteId").toString());
        atencion.setPaciente(pacienteRepo.findById(pacienteId).get());

        Long enfermeroId = Long.valueOf(datos.get("enfermeroId").toString());
        atencion.setPersonal(personalRepo.findById(enfermeroId).get());

        if (datos.get("insumoIds") != null) {
            List<Integer> idsRaw = (List<Integer>) datos.get("insumoIds");
            List<Long> ids = idsRaw.stream().map(Integer::longValue).toList();
            atencion.setInsumos(insumoRepo.findAllById(ids));
        }

        return atencionRepo.save(atencion);
    }

    // 5. ELIMINAR ATENCIÓN
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        atencionRepo.deleteById(id);
        return "Atención eliminada correctamente";
    }
}