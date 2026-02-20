package com.enfermeria.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_enfermeria")
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column
    private String turno; // "Mañana", "Tarde", "Noche"

    @Column
    private String telefono;

    @Column
    private String correo;

    @Column
    private String puesto; // "Jefe de Enfermería", "Auxiliar"

    // Constructor vacío (Obligatorio para Spring Boot/JPA)
    public Personal() {
    }

    // Constructor con parámetros 
    public Personal(Long id, String nombre, String apellido, String turno, String telefono, String correo, String puesto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.turno = turno;
        this.telefono = telefono;
        this.correo = correo;
        this.puesto = puesto;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
}