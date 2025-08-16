package com.ipss.demo.service;

import com.ipss.demo.dto.CrearReservaDTO;
import com.ipss.demo.model.Mesa;
import com.ipss.demo.model.Reserva;
import com.ipss.demo.repository.MesaRepository;
import com.ipss.demo.repository.ReservaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final MesaRepository mesaRepo;

    public ReservaService(ReservaRepository reservaRepo, MesaRepository mesaRepo) {
        this.reservaRepo = reservaRepo;
        this.mesaRepo = mesaRepo;
    }

    // --- Crear reserva ---
    public Reserva crear(CrearReservaDTO dto) {
        Mesa mesa = mesaRepo.findById(dto.getMesaId())
                .orElseThrow(() -> new IllegalArgumentException("Mesa no existe: " + dto.getMesaId()));

        LocalDateTime inicio = dto.getInicio();
        LocalDateTime fin = inicio.plusMinutes(dto.getMinutos());

        // regla básica: no traslapes en la misma mesa
        boolean solapa = reservaRepo.existsByMesaIdAndInicioLessThanAndFinGreaterThan(
                mesa.getId(), fin, inicio
        );
        if (solapa) {
            throw new IllegalArgumentException("La mesa ya está reservada en ese rango.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "anon";

        Reserva r = new Reserva();
        r.setMesa(mesa);
        r.setInicio(inicio);
        r.setFin(fin);
        r.setEstado("ACTIVA");
        r.setUsername(username);

        return reservaRepo.save(r);
    }

    // --- Historico del usuario autenticado ---
    public List<Reserva> misReservas(String username) {
        return reservaRepo.findByUsernameOrderByInicioDesc(username);
    }

    // (opcional, si lo usabas antes)
    public List<Reserva> misReservas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "anon";
        return misReservas(username);
    }

    // --- Cancelar ---
    public void cancelar(Long id) {
        Reserva r = reservaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no existe: " + id));
        r.setEstado("CANCELADA");
        reservaRepo.save(r);
    }

    // --- Listado completo (para comensal/admin) ---
    public List<Reserva> listarTodas() {
        List<Reserva> all = reservaRepo.findAll();
        all.sort(Comparator.comparing(Reserva::getInicio).reversed());
        return all;
    }
}
