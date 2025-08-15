package com.ipss.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Seguridad para API REST + HTML/JS estático (sin Thymeleaf).
 * - Vistas públicas: /, /index.html, /cliente.html y assets estáticos.
 * - Vista de administración: /admin.html (requiere ROLE_ADMIN).
 * - API Mesas:
 *     GET   /api/mesas/**        -> público
 *     POST/PUT/DELETE            -> ROLE_ADMIN
 * - Habilita @PreAuthorize en controladores (@EnableMethodSecurity).
 * - Autenticación básica (httpBasic). Si prefieres formLogin, avísame.
 */

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // ====== Usuarios en memoria (demo) ======
    // admin / admin123  -> ROLE_ADMIN
    // cliente / cliente123 -> ROLE_USER
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails cliente = User.withUsername("cliente")
                .password(encoder.encode("cliente123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, cliente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ====== Reglas de autorización por ruta y método ======
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Para simplificar peticiones fetch desde HTML estático
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // Vistas y recursos públicos
                .requestMatchers("/", "/index.html", "/cliente.html",
                                 "/css/**", "/js/**", "/images/**", "/assets/**").permitAll()

                // Vista admin solo para ROLE_ADMIN
                .requestMatchers("/admin.html").hasRole("ADMIN")

                // API Mesas: GET es público; mutaciones solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/mesas/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/mesas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/mesas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/mesas/**").hasRole("ADMIN")

                // Cualquier otra petición, autenticada
                .anyRequest().authenticated()
            )

            // Autenticación básica; si prefieres formulario: .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
