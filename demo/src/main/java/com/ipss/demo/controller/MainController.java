package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages") // <— prefijo distinto
public class MainController {

    @GetMapping("/admin")
    public String admin() { return "admin"; }

    @GetMapping("/cliente")
    public String cliente() { return "cliente"; }

    @GetMapping("/comensal")
    public String comensal() { return "comensal"; }
}
// Quedarán en /pages/admin, /pages/cliente, /pages/comensal
