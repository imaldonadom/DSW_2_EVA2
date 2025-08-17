package com.ipss.demo.dto;

import java.time.*;

public record CrearDisponibilidadDTO(Long mesaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {}

