package com.ipss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    public enum Estado { PENDIENTE, CONFIRMADA, CANCELADA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos servicios/controladores esperan esto:
    @ManyToOne(optional = false)
    private Mesa mesa;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column(nullable = false)
    private LocalDateTime fin;

    // Para listados por usuario:
    @Column(nullable = true)
    private String username;

    // Si además usas disponibilidad en otros flujos, lo dejamos:
    @ManyToOne
    private Disponibilidad disponibilidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    // === getters/setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    // Sobrecarga para cuando te pasan un String:
    public void setEstado(String estadoTexto) {
        if (estadoTexto == null) return;
        try {
            this.estado = Estado.valueOf(estadoTexto.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            this.estado = Estado.PENDIENTE;
        }
    }
}
