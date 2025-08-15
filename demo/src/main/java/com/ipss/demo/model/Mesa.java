package com.ipss.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Mesa {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull @Min(1)
  private Integer numero;

  @NotNull @Min(1)
  private Integer capacidad;

  private String ubicacion;
  private Boolean fumadores = false;

  public Long getId() { return id; }
  public Integer getNumero() { return numero; }
  public void setNumero(Integer numero) { this.numero = numero; }
  public Integer getCapacidad() { return capacidad; }
  public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
  public String getUbicacion() { return ubicacion; }
  public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
  public Boolean getFumadores() { return fumadores; }
  public void setFumadores(Boolean fumadores) { this.fumadores = fumadores; }
}
