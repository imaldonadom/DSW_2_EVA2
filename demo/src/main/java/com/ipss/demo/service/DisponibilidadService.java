package com.ipss.demo.service;

import com.ipss.demo.model.Disponibilidad;
import com.ipss.demo.repository.DisponibilidadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository repo;

    public DisponibilidadService(DisponibilidadRepository repo) {
        this.repo = repo;
    }

    public List<Disponibilidad> listarPorMesaYFecha(Long mesaId, LocalDate fecha) {
        return repo.findByMesaIdAndFecha(mesaId, fecha);
    }

    public List<Disponibilidad> listarPorFecha(LocalDate fecha) {
        return repo.findByFecha(fecha);
    }

    public List<Disponibilidad> listarPorFechaOrdenado(LocalDate fecha) {
        return repo.findByFechaOrderByMesaIdAscAperturaAsc(fecha);
    }

    public Disponibilidad crear(Long mesaId, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        Disponibilidad d = new Disponibilidad();
        // setear solo referencia de Mesa por id (ajusta si ya cargas la entidad)
        com.ipss.demo.model.Mesa m = new com.ipss.demo.model.Mesa();
        m.setId(mesaId.intValue());
        d.setMesa(m);
        d.setFecha(fecha);
        d.setApertura(inicio);
        d.setCierre(fin);
        d.setEstado(Disponibilidad.Estado.DISPONIBLE);
        return repo.save(d);
    }

    public Disponibilidad cambiarEstado(Long id, Disponibilidad.Estado nuevo) {
        Disponibilidad d = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada: " + id));
        d.setEstado(nuevo);
        return repo.save(d);
    }

    public List<Disponibilidad> disponiblesEn(LocalDate f) {
        throw new UnsupportedOperationException("Unimplemented method 'disponiblesEn'");
    }
}
