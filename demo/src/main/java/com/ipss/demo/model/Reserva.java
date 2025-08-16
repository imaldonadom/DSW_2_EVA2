package com.ipss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mesaId;          // <-- Integer
    private String username;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String estado;

    public Long getId() { return id; }
    public Integer getMesaId() { return mesaId; }
    public void setMesaId(Integer mesaId) { this.mesaId = mesaId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
