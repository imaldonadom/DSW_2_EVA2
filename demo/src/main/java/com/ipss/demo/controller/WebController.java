package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // ¡OJO! Ya NO mapeamos "/", ni "/index" aquí
    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/access-denied")
    public String denied() { return "error/403"; }
}
