package com.ipss.demo.dto;

import java.time.LocalDateTime;

public class CrearReservaDTO {
    private Integer mesaId;
    private LocalDateTime inicio;
    private int minutos;

    public Integer getMesaId() { return mesaId; }
    public void setMesaId(Integer mesaId) { this.mesaId = mesaId; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public int getMinutos() { return minutos; }
    public void setMinutos(int minutos) { this.minutos = minutos; }
}
