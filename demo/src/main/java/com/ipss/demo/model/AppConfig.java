package com.ipss.demo.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "app_config")
public class AppConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Horario de atención (por defecto 12:00–23:00)
    private LocalTime apertura;
    private LocalTime cierre;

    public AppConfig() { }
    public AppConfig(LocalTime apertura, LocalTime cierre) {
        this.apertura = apertura;
        this.cierre = cierre;
    }

    public Long getId() { return id; }
    public LocalTime getApertura() { return apertura; }
    public void setApertura(LocalTime apertura) { this.apertura = apertura; }
    public LocalTime getCierre() { return cierre; }
    public void setCierre(LocalTime cierre) { this.cierre = cierre; }
}

