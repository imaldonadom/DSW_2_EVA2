package com.ipss.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Mesa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull @Min(1)
    private Integer numero;

    @NotNull @Min(1)
    private Integer capacidad;

    private String ubicacion;      // Interior / Terraza / Patio (libre)
    private Boolean fumadores;     // true/false

    // getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Boolean getFumadores() { return fumadores; }
    public void setFumadores(Boolean fumadores) { this.fumadores = fumadores; }
}
