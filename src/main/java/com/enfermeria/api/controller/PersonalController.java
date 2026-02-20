package com.enfermeria.api.controller;

import com.enfermeria.api.entity.Personal;
import com.enfermeria.api.repository.PersonalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    private final PersonalRepository repo;

    // Inyección de dependencias a través del constructor
    public PersonalController(PersonalRepository repo) {
        this.repo = repo;
    }

    // 1. LISTAR TODOS (GET a /personal)
    @GetMapping
    public List<Personal> listar() {
        return repo.findAll();
    }

    // 2. BUSCAR POR ID (GET a /personal/1)
    @GetMapping("/{id}")
    public ResponseEntity<Personal> buscarPorId(@PathVariable Long id) {
        Optional<Personal> personal = repo.findById(id);
        if (personal.isPresent()) {
            return ResponseEntity.ok(personal.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. BUSCADOR POR NOMBRE (GET a /personal/buscar?nombre=Paolo)
    @GetMapping("/buscar")
    public List<Personal> buscarPorNombre(@RequestParam String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    // 4. GUARDAR / CREAR (POST a /personal)
    @PostMapping
    public Personal guardar(@RequestBody Personal p) {
        return repo.save(p);
    }

    // 5. ACTUALIZAR (PUT a /personal/1)
    @PutMapping("/{id}")
    public ResponseEntity<Personal> actualizar(@PathVariable Long id, @RequestBody Personal p) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        p.setId(id); // Aseguramos que se actualice el ID correcto
        return ResponseEntity.ok(repo.save(p));
    }

    // 6. ELIMINAR (DELETE a /personal/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna código 204 (Éxito sin contenido)
    }
}