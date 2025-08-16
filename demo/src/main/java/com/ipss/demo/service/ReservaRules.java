package com.ipss.demo.service;

import com.ipss.demo.model.AppConfig;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ReservaRules {
    private final AppConfigService cfgService;
    public ReservaRules(AppConfigService cfgService) { this.cfgService = cfgService; }

    /**
     * Valida que la reserva esté dentro del horario laboral.
     * Regla típica: si el restaurante trabaja de 12:00 a 23:00,
     * NO se permite agendar 23:01–11:59 del día siguiente.
     */
    public void validarHorario(LocalDateTime inicio, int minutos) {
        AppConfig cfg = cfgService.getOrCreate();
        LocalTime apertura = cfg.getApertura();   // ej. 12:00
        LocalTime cierre   = cfg.getCierre();     // ej. 23:00

        LocalTime tIni = inicio.toLocalTime();
        LocalTime tFin = inicio.plusMinutes(minutos).toLocalTime();

        boolean overnight = cierre.isBefore(apertura); // horario que cruza medianoche (ej. 18:00–02:00)

        boolean dentro;
        if (!overnight) {
            // Apertura < Cierre típico (12:00–23:00)
            dentro = !tIni.isBefore(apertura) && !tFin.isAfter(cierre);
        } else {
            // Caso nocturno (por si algún día lo usas): válido si cae en [apertura..23:59] o [00:00..cierre]
            boolean iniOK = !tIni.isBefore(apertura) || !tIni.isAfter(cierre);
            boolean finOK = !tFin.isBefore(apertura) || !tFin.isAfter(cierre);
            dentro = iniOK && finOK;
        }

        if (!dentro) {
            throw new IllegalArgumentException(
                "Reserva fuera del horario laboral: " + apertura + " a " + cierre);
        }
    }
}

