package com.enfermeria.api.repository;
import com.enfermeria.api.entity.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface PersonalRepository extends JpaRepository<Personal, Long> {

    // Buscador:Encuentra por nombre (ignorando mayúsculas/minúsculas)
    // Ejemplo:buscar "Juan" encuentra "juan", "JUAN", "Juan Carlos"
    List<Personal> findByNombreContainingIgnoreCase(String nombre);

}