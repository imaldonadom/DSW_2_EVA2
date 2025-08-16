package com.ipss.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthDebugController {

    @GetMapping("/me")
    public Map<String, Object> me() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) {
            return Map.of("error", "No autenticado");
        }
        return Map.of(
            "user", a.getName(),
            "authorities", a.getAuthorities()
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList())
        );
    }
}
