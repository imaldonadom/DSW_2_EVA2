package com.ipss.demo.service;

import com.ipss.demo.model.Disponibilidad;
import com.ipss.demo.model.Mesa;
import com.ipss.demo.model.Reserva;
import com.ipss.demo.repository.DisponibilidadRepository;
import com.ipss.demo.repository.MesaRepository;
import com.ipss.demo.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservaClienteService {

    private final ReservaRepository reservaRepo;
    private final DisponibilidadRepository dispRepo;
    private final MesaRepository mesaRepo;

    public ReservaClienteService(ReservaRepository reservaRepo,
                                 DisponibilidadRepository dispRepo,
                                 MesaRepository mesaRepo) {
        this.reservaRepo = reservaRepo;
        this.dispRepo = dispRepo;
        this.mesaRepo = mesaRepo;
    }

    public Reserva reservar(Long mesaId,
                            Long disponibilidadId,
                            String username,
                            String telefono,
                            int comensales) {

        Mesa mesa = mesaRepo.findById(mesaId.intValue())
            .orElseThrow(() -> new IllegalArgumentException("Mesa no existe: " + mesaId));

        Disponibilidad d = dispRepo.findById(disponibilidadId)
            .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no existe: " + disponibilidadId));

        LocalDateTime inicio = LocalDateTime.of(d.getFecha(), d.getApertura());
        LocalDateTime fin    = LocalDateTime.of(d.getFecha(), d.getCierre());

        Reserva r = new Reserva();
        r.setMesa(mesa);
        r.setDisponibilidad(d);
        r.setInicio(inicio);
        r.setFin(fin);
        r.setUsername(username != null && !username.isBlank() ? username : "cliente");
        r.setEstado(Reserva.Estado.PENDIENTE);
        return reservaRepo.save(r);
    }

    public Reserva cancelar(Long reservaId) {
        Reserva r = reservaRepo.findById(reservaId)
            .orElseThrow(() -> new IllegalArgumentException("Reserva no existe: " + reservaId));
        r.setEstado(Reserva.Estado.CANCELADA);
        return reservaRepo.save(r);
    }
}
