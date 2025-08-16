package com.ipss.demo.service;

import com.ipss.demo.dto.CrearReservaDTO;
import com.ipss.demo.model.Reserva;
import java.util.List;

public interface ReservaService {
    Reserva crear(CrearReservaDTO dto);
    List<?> misReservas();
    void cancelar(Long id);
    List<?> listarTodas();
}
