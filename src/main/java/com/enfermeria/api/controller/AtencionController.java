package com.enfermeria.api.controller;

public class AtencionController {
}
package com.enfermeria.enfermeria_app.controller;

import com.enfermeria.enfermeria_app.entity.Atencion;
import com.enfermeria.enfermeria_app.entity.Paciente;
import com.enfermeria.enfermeria_app.entity.PersonalEnfermeria;
import com.enfermeria.enfermeria_app.repository.AtencionRepository;
import com.enfermeria.enfermeria_app.repository.PacienteRepository;
import com.enfermeria.enfermeria_app.repository.PersonalEnfermeriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionRepository atencionRepository;
    private final PacienteRepository pacienteRepository;
    private final PersonalEnfermeriaRepository personalRepository;

    public AtencionController(
            AtencionRepository atencionRepository,
            PacienteRepository pacienteRepository,
            PersonalEnfermeriaRepository personalRepository) {
        this.atencionRepository = atencionRepository;
        this.pacienteRepository = pacienteRepository;
        this.personalRepository = personalRepository;
    }

    // GET: Obtener todas las atenciones
    @GetMapping
    public List<Atencion> listarTodas() {
        return atencionRepository.findAll();
    }

    // GET: Obtener atención por ID
    @GetMapping("/{id}")
    public ResponseEntity<Atencion> obtenerPorId(@PathVariable Long id) {
        return atencionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET: Buscar por ID de paciente
    @GetMapping("/paciente/{pacienteId}")
    public List<Atencion> listarPorPaciente(@PathVariable Long pacienteId) {
        // Verificar si el paciente existe
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Paciente no encontrado con id: " + pacienteId);
        }
        return atencionRepository.findByPacienteId(pacienteId);
    }

    // GET: Buscar por ID de personal
    @GetMapping("/personal/{personalId}")
    public List<Atencion> listarPorPersonal(@PathVariable Long personalId) {
        // Verificar si el personal existe
        if (!personalRepository.existsById(personalId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Personal no encontrado con id: " + personalId);
        }
        return atencionRepository.findByPersonalId(personalId);
    }

    // POST: Crear nueva atención
    @PostMapping
    public ResponseEntity<Atencion> crearAtencion(@RequestBody AtencionRequest request) {

        // Buscar paciente por ID
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado con id: " + request.getPacienteId()));

        // Buscar personal por ID
        PersonalEnfermeria personal = personalRepository.findById(request.getPersonalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Personal no encontrado con id: " + request.getPersonalId()));

        // Crear nueva atención
        Atencion nuevaAtencion = new Atencion();
        nuevaAtencion.setFecha(request.getFecha());
        nuevaAtencion.setMotivo(request.getMotivo());
        nuevaAtencion.setDiagnostico(request.getDiagnostico());
        nuevaAtencion.setTratamiento(request.getTratamiento());
        nuevaAtencion.setPaciente(paciente);
        nuevaAtencion.setPersonal(personal);

        Atencion guardada = atencionRepository.save(nuevaAtencion);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    // PUT: Actualizar atención existente
    @PutMapping("/{id}")
    public ResponseEntity<Atencion> actualizarAtencion(
            @PathVariable Long id,
            @RequestBody AtencionRequest request) {

        // Buscar atención existente
        Atencion atencionExistente = atencionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Atención no encontrada con id: " + id));

        // Buscar paciente (si se proporciona nuevo)
        if (request.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Paciente no encontrado con id: " + request.getPacienteId()));
            atencionExistente.setPaciente(paciente);
        }

        // Buscar personal (si se proporciona nuevo)
        if (request.getPersonalId() != null) {
            PersonalEnfermeria personal = personalRepository.findById(request.getPersonalId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Personal no encontrado con id: " + request.getPersonalId()));
            atencionExistente.setPersonal(personal);
        }

        // Actualizar campos básicos (solo si vienen en la petición)
        if (request.getFecha() != null) atencionExistente.setFecha(request.getFecha());
        if (request.getMotivo() != null) atencionExistente.setMotivo(request.getMotivo());
        if (request.getDiagnostico() != null) atencionExistente.setDiagnostico(request.getDiagnostico());
        if (request.getTratamiento() != null) atencionExistente.setTratamiento(request.getTratamiento());

        Atencion actualizada = atencionRepository.save(atencionExistente);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE: Eliminar atención
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtencion(@PathVariable Long id) {
        if (!atencionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        atencionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

// Clase DTO para recibir peticiones (puedes crearla en un archivo aparte o como inner class)
class AtencionRequest {
    private LocalDate fecha;
    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private Long pacienteId;
    private Long personalId;

    // Getters y Setters
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getPersonalId() { return personalId; }
    public void setPersonalId(Long personalId) { this.personalId = personalId; }
}