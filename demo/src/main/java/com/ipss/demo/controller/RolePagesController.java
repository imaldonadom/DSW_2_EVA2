package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RolePagesController {

    @GetMapping("/cliente")
    public String cliente() { return "cliente"; }

    @GetMapping("/comensal")
    public String comensal() { return "comensal"; }

    @GetMapping("/admin")
    public String admin() { return "admin"; }

    @GetMapping("/admin/usuarios")
    public String adminUsuarios() { return "admin-usuarios"; }

    @GetMapping("/admin/mesas")
    public String adminMesas() { return "admin-mesas"; }

    @GetMapping("/admin/disponibilidad")
    public String adminDisponibilidad() { return "admin-disponibilidad"; }

    
}
