package com.ipss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Mesa mesa;               // <-- relacionamos con Mesa

    private LocalDateTime inicio;
    private LocalDateTime fin;

    // opcionales (si no los usas, quítalos y también sus usos)
    private String estado = "ACTIVA";
    private String username;

    // --- getters/setters ---
    public Long getId() { return id; }

    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    
}
