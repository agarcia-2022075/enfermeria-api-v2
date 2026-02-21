package com.enfermeria.api.controller;

import com.enfermeria.api.entity.Paciente;
import com.enfermeria.api.repository.PacienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository repo;

    public PacienteController(PacienteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Paciente> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Paciente> buscarPorId(@PathVariable Long id) {
        return repo.findById(id);
    }

    @PostMapping
    public Paciente guardar(@RequestBody Paciente p) {
        return repo.save(p);
    }

    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable Long id, @RequestBody Paciente p) {
        p.setId(id);
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "Paciente eliminado correctamente";
    }

    @GetMapping("/buscar")
    public List<Paciente> buscarPorNombre(@RequestParam String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}