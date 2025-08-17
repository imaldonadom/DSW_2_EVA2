package com.ipss.demo.repository;

import com.ipss.demo.model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Los controladores te pasan mesaId como Integer
    List<Reserva> findByMesaIdAndInicioBetween(Integer mesaId, LocalDateTime desde, LocalDateTime hasta);

    boolean existsByMesaIdAndInicioLessThanAndFinGreaterThan(Integer mesaId, LocalDateTime nuevoFin, LocalDateTime nuevoInicio);

    List<Reserva> findByUsernameOrderByInicioDesc(String username);
}
