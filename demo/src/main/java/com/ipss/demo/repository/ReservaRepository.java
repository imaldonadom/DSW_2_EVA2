package com.ipss.demo.repository;

import com.ipss.demo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Para detectar solapamiento en la misma mesa
    boolean existsByMesaIdAndInicioLessThanAndFinGreaterThan(
            Integer mesaId, LocalDateTime finNuevo, LocalDateTime inicioNuevo
    );

    // Historial de un usuario (mismas que usas en misReservas)
    List<Reserva> findByUsernameOrderByInicioDesc(String username);

    // Consulta por rango y mesa (si la usas para disponibilidad)
    List<Reserva> findByMesaIdAndInicioBetween(Integer mesaId, LocalDateTime desde, LocalDateTime hasta);
}
