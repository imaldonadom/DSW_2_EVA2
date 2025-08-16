package com.ipss.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // habilita @PreAuthorize en controllers/services
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // La API no usa CSRF (solo vistas)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            .authorizeHttpRequests(auth -> auth
                // público
                .requestMatchers("/", "/index.html", "/login",
                                 "/register", "/register.html",
                                 "/css/**", "/js/**", "/images/**",
                                 "/webjars/**", "/favicon.ico").permitAll()

                // vistas por rol
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/comensal/**").hasAnyRole("COMENSAL","ADMIN")
                .requestMatchers("/cliente/**").hasAnyRole("CLIENTE","ADMIN")

                // API por rol
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/reservas/**").hasAnyRole("ADMIN","COMENSAL","CLIENTE")
                .requestMatchers("/api/mesas/**").permitAll() // GET público

                // lo demás, autenticado
                .anyRequest().authenticated()
            )

            .formLogin(f -> f
                .loginPage("/login")
                .successHandler((req, res, auth) -> {
                    // redirección por rol
                    var roles = auth.getAuthorities().toString();
                    if (roles.contains("ROLE_ADMIN")) {
                        res.sendRedirect("/admin");
                    } else if (roles.contains("ROLE_COMENSAL")) {
                        res.sendRedirect("/comensal");
                    } else {
                        res.sendRedirect("/cliente");
                    }
                })
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(l -> l
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
