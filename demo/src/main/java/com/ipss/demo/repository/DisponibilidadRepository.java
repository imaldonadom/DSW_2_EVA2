package com.ipss.demo.repository;

import com.ipss.demo.model.Disponibilidad;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface DisponibilidadRepository extends CrudRepository<Disponibilidad, Long> {

    List<Disponibilidad> findByMesaIdAndFecha(Long mesaId, LocalDate fecha);

    List<Disponibilidad> findByFecha(LocalDate fecha);

    // útil para listar bonito por mesa y hora
    List<Disponibilidad> findByFechaOrderByMesaIdAscAperturaAsc(LocalDate fecha);
}
