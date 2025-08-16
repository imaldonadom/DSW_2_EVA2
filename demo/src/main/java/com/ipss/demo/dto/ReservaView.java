package com.ipss.demo.dto;

import java.time.LocalDateTime;

public class ReservaView {
    public Long id;
    public Integer mesaNumero;
    public String username;
    public LocalDateTime inicio;
    public LocalDateTime fin;
    public String estado;

    public ReservaView(Long id, Integer mesaNumero, String username,
                       LocalDateTime inicio, LocalDateTime fin, String estado) {
        this.id = id; this.mesaNumero = mesaNumero; this.username = username;
        this.inicio = inicio; this.fin = fin; this.estado = estado;
    }
}
