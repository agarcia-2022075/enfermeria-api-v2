package com.enfermeria.api.repository;

import com.enfermeria.api.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "atenciones")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 200)
    private String motivo;

    @Column(nullable = false, length = 500)
    private String diagnostico;

    @Column(nullable = false, length = 500)
    private String tratamiento;

    // Relación ManyToOne con Paciente
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Relación ManyToOne con PersonalEnfermeria
    @ManyToOne
    @JoinColumn(name = "personal_id", nullable = false)
    private PersonalEnfermeria personal;

    // Constructor vacío obligatorio para JPA
    public Atencion() {}

    // Getters y Setters
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

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public PersonalEnfermeria getPersonal() { return personal; }
    public void setPersonal(PersonalEnfermeria personal) { this.personal = personal; }
}