package com.ipss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidades",
       uniqueConstraints = @UniqueConstraint(columnNames = {"mesa_id","fecha","apertura"}))
public class Disponibilidad {

    // ← Los controladores usan estos nombres
    public enum Estado { DISPONIBLE, NO_DISPONIBLE }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime apertura;

    @Column(nullable = false)
    private LocalTime cierre;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Estado estado = Estado.DISPONIBLE;

    // ==== getters/setters ====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getApertura() { return apertura; }
    public void setApertura(LocalTime apertura) { this.apertura = apertura; }

    public LocalTime getCierre() { return cierre; }
    public void setCierre(LocalTime cierre) { this.cierre = cierre; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
