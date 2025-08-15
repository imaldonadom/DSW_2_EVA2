package com.ipss.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // NADA en "/". La raíz la sirve index.html estático.

    @GetMapping("/api/hello")
    public String hello() {
        return "API OK";
    }
}
