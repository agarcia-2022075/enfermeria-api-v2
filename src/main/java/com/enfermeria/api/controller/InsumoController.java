package com.enfermeria.api.controller;

import com.enfermeria.api.repository.InsumoRepository;
import com.enfermeria.api.entity.Insumo;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/insumos")
public class InsumoController {

    private final InsumoRepository repo;

    public InsumoController(InsumoRepository repo) {
        this.repo = repo;
    }

    // GET all - Listar todos los insumos
    @GetMapping
    public List<Insumo> listar() {
        return repo.findAll();
    }

    // GET by ID - Buscar insumo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Insumo> obtenerPorId(@PathVariable Long id) {
        Optional<Insumo> insumo = repo.findById(id);
        if (insumo.isPresent()) {
            return ResponseEntity.ok(insumo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear nuevo insumo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Insumo guardar(@RequestBody Insumo insumo) {
        return repo.save(insumo);
    }

    // PUT - Actualizar insumo existente
    @PutMapping("/{id}")
    public ResponseEntity<Insumo> actualizar(@PathVariable Long id, @RequestBody Insumo insumoActualizado) {
        Optional<Insumo> insumoOptional = repo.findById(id);

        if (insumoOptional.isPresent()) {
            Insumo insumoExistente = insumoOptional.get();
            insumoExistente.setNombre(insumoActualizado.getNombre());
            insumoExistente.setTipo(insumoActualizado.getTipo());
            insumoExistente.setCantidad(insumoActualizado.getCantidad());
            insumoExistente.setFechaVencimiento(insumoActualizado.getFechaVencimiento());
            insumoExistente.setProveedor(insumoActualizado.getProveedor());

            Insumo guardado = repo.save(insumoExistente);
            return ResponseEntity.ok(guardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Eliminar insumo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}