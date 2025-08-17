package com.ipss.demo.service;

import com.ipss.demo.dto.CrearReservaDTO;
import com.ipss.demo.model.Mesa;
import com.ipss.demo.model.Reserva;
import com.ipss.demo.repository.MesaRepository;
import com.ipss.demo.repository.ReservaRepository;
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

    public Reserva findById(Long id) {
        return reservaRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + id));
    }

    public List<Reserva> listarPorMesaYFechas(Integer mesaId, LocalDateTime desde, LocalDateTime hasta) {
        List<Reserva> out = reservaRepo.findByMesaIdAndInicioBetween(mesaId, desde, hasta);
        out.sort(Comparator.comparing(Reserva::getInicio));
        return out;
    }

    public boolean existeSolape(Integer mesaId, LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepo.existsByMesaIdAndInicioLessThanAndFinGreaterThan(mesaId, fin, inicio);
    }

    // ---------- métodos usados por tus controladores ----------

    public Reserva crear(CrearReservaDTO dto) {
        Mesa mesa = mesaRepo.findById(dto.getMesaId())
            .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + dto.getMesaId()));

        Reserva r = new Reserva();
        r.setMesa(mesa);
        r.setInicio(dto.getInicio());

        // Tu DTO no tiene getFin(): asumimos bloque de 1 hora
        LocalDateTime fin = dto.getInicio().plusHours(1);
        r.setFin(fin);

        // Tu DTO no tiene getUsername(): dejamos un valor genérico
        if (r.getUsername() == null || r.getUsername().isBlank()) {
            r.setUsername("cliente");
        }

        r.setEstado(Reserva.Estado.PENDIENTE);
        return reservaRepo.save(r);
    }

    public List<Reserva> misReservas(String username) {
        return reservaRepo.findByUsernameOrderByInicioDesc(username);
    }

    public void cancelar(Long id) {
        Reserva r = findById(id);
        r.setEstado(Reserva.Estado.CANCELADA);
        reservaRepo.save(r);
    }

    // Tu controlador llama listarTodas(): la exponemos con ese nombre.
    public List<Reserva> listarTodas() {
        return reservaRepo.findAll();
    }

    // (Alias opcional por si en otro lado usaste listarTodos)
    public List<Reserva> listarTodos() {
        return listarTodas();
    }
}
