package com.ipss.demo.controller;

import com.ipss.demo.model.Reserva;
import com.ipss.demo.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/reservas")
public class ReservaAdminController {

    private final ReservaService reservaService;

    public ReservaAdminController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public String pagina(Model model) {
        List<Reserva> items = reservaService.listarTodas();
        model.addAttribute("items", items);
        return "admin-reservas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id) {
        reservaService.cancelar(id);
        return "redirect:/admin/reservas";
    }
}
