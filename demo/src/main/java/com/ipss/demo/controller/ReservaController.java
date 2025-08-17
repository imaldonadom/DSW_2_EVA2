package com.ipss.demo.controller;

import com.ipss.demo.model.Reserva;
import com.ipss.demo.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Lista general
    @GetMapping("/todas")
    public String listarTodas(Model model) {
        List<Reserva> items = reservaService.listarTodas();
        model.addAttribute("items", items);
        return "reserva-lista";
    }

    // Cancelación genérica
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        reservaService.cancelar(id);
        return "redirect:/reserva/todas";
    }

    // Filtro por mesa y rango de fechas
    @GetMapping("/mesa/{mesaId}")
    public String porMesaYRango(@PathVariable Integer mesaId,
                                @RequestParam String desde,
                                @RequestParam String hasta,
                                Model model) {
        LocalDateTime d = LocalDateTime.parse(desde);
        LocalDateTime h = LocalDateTime.parse(hasta);
        List<Reserva> items = reservaService.listarPorMesaYFechas(mesaId, d, h);
        model.addAttribute("items", items);
        return "reserva-lista";
    }

    // Comprobación de solape
    @GetMapping("/existe-solape")
    @ResponseBody
    public boolean existeSolape(@RequestParam Integer mesaId,
                                @RequestParam String inicio,
                                @RequestParam String fin) {
        LocalDateTime i = LocalDateTime.parse(inicio);
        LocalDateTime f = LocalDateTime.parse(fin);
        return reservaService.existeSolape(mesaId, i, f);
    }
}
