package com.ipss.demo.service.impl;

import com.ipss.demo.dto.CrearReservaDTO;
import com.ipss.demo.dto.ReservaView;
import com.ipss.demo.model.Mesa;
import com.ipss.demo.model.Reserva;
import com.ipss.demo.repository.MesaRepository;
import com.ipss.demo.repository.ReservaRepository;
import com.ipss.demo.service.ReservaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepo;
    private final MesaRepository mesaRepo;

    public ReservaServiceImpl(ReservaRepository reservaRepo, MesaRepository mesaRepo) {
        this.reservaRepo = reservaRepo;
        this.mesaRepo = mesaRepo;
    }

    private String currentUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return a != null ? a.getName() : "anon";
    }

    @Override
    public Reserva crear(CrearReservaDTO dto) {
        if (dto.getMinutos() < 60 || dto.getMinutos() > 180) {
            throw new IllegalArgumentException("Duración permitida: 60, 120 o 180 minutos.");
        }
        LocalDateTime inicio = dto.getInicio();
        LocalDateTime fin = inicio.plusMinutes(dto.getMinutos());

        // Integer en todas partes
       mesaRepo.findById(dto.getMesaId())
                .orElseThrow(() -> new NoSuchElementException("Mesa no existe"));

        boolean ocupado = reservaRepo.existsByMesaIdAndEstadoAndInicioLessThanAndFinGreaterThan(
                dto.getMesaId(), "RESERVADO", fin, inicio);
        if (ocupado) throw new IllegalArgumentException("La mesa ya está reservada en ese horario.");

        Reserva r = new Reserva();
        r.setMesaId(dto.getMesaId());
        r.setUsername(currentUser());
        r.setInicio(inicio);
        r.setFin(fin);
        r.setEstado("RESERVADO");
        return reservaRepo.save(r);
    }

    @Override
    public List<?> misReservas() {
        String u = currentUser();
        return reservaRepo.findByUsernameOrderByInicioDesc(u)
                .stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    public void cancelar(Long id) {
        Reserva r = reservaRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Reserva no existe"));
        String u = currentUser();
        if (!u.equals(r.getUsername())) {
            throw new IllegalArgumentException("No puedes cancelar reservas de otro usuario.");
        }
        r.setEstado("CANCELADO");
        reservaRepo.save(r);
    }

    @Override
    public List<?> listarTodas() {
        return reservaRepo.findAll().stream().map(this::toView).collect(Collectors.toList());
    }

    private ReservaView toView(Reserva r) {
        Integer mesaNumero = null;
        if (r.getMesaId() != null) {
            mesaNumero = mesaRepo.findById(r.getMesaId())
                    .map(Mesa::getNumero).orElse(null);
        }
        return new ReservaView(r.getId(), mesaNumero, r.getUsername(),
                r.getInicio(), r.getFin(), r.getEstado());
    }
}
