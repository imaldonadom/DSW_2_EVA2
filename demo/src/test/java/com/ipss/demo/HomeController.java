package com.ipss.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("mensaje", "Bienvenido/a 👋 – Thymeleaf está OK.");
    return "index"; // busca templates/index.html
  }
}
