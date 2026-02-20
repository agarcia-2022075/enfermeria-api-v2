package com.enfermeria.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "atenciones")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate fecha;

    @Column
    private String motivo;

    @Column
    private String diagnostico;

    @Column
    private String tratamiento;

    // --- MAGIA RELACIONAL ---

    // Relación: Muchas atenciones pueden pertenecer a 1 Paciente
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false) // Esto crea la Foreign Key 'paciente_id' en MySQL
    private Paciente paciente;

    // Relación: Muchas atenciones pueden ser realizadas por 1 Personal (Enfermero/Doctor)
    @ManyToOne
    @JoinColumn(name = "personal_id", nullable = false) // Esto crea la Foreign Key 'personal_id' en MySQL
    private Personal personal;

    // Relación: Muchas atenciones pueden usar Muchos insumos (y viceversa)
    @ManyToMany
    @JoinTable(
            name = "atencion_insumos", // Nombre de la tabla intermedia (Tabla Pivote) que se creará automáticamente
            joinColumns = @JoinColumn(name = "atencion_id"), // La llave foránea que apunta a esta atención
            inverseJoinColumns = @JoinColumn(name = "insumo_id") // La llave foránea que apunta al insumo
    )
    private List<Insumo> insumos;

    // Constructor vacío (Obligatorio para Spring Boot/JPA)
    public Atencion() {
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    // Getters y Setters de los OBJETOS relacionados
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Personal getPersonal() { return personal; }
    public void setPersonal(Personal personal) { this.personal = personal; }

    public List<Insumo> getInsumos() { return insumos; }
    public void setInsumos(List<Insumo> insumos) { this.insumos = insumos; }
}