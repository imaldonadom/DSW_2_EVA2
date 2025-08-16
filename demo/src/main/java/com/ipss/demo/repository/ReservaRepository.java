package com.ipss.demo.repository;

import com.ipss.demo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByMesaIdAndEstadoAndInicioLessThanAndFinGreaterThan(
            Integer mesaId, String estado, LocalDateTime fin, LocalDateTime inicio
    );

    List<Reserva> findByUsernameOrderByInicioDesc(String username);
}
