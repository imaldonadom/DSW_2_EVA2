package com.ipss.demo.controller;

import com.ipss.demo.model.AppUser;
import com.ipss.demo.model.Role;
import com.ipss.demo.repository.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AppUserRepository repo;
    private final BCryptPasswordEncoder encoder;

    public AuthController(AppUserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @GetMapping({"/", "/index"})
    public String index() { return "index"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register(Model m) {
        m.addAttribute("user", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("user") @Valid AppUser u, Model m) {
        if (repo.existsByUsername(u.getUsername())) {
            m.addAttribute("error", "El usuario ya existe");
            return "register";
        }
        u.setRole(Role.CLIENTE);
        u.setPassword(encoder.encode(u.getPassword()));
        repo.save(u);
        m.addAttribute("ok", "Cuenta creada. Inicia sesión.");
        return "login";
    }
}
