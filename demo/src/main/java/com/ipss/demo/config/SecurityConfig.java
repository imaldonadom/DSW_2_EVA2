package com.ipss.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, RoleRedirectHandler successHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/comensal/**").hasRole("COMENSAL")
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .successHandler(successHandler) // redirige por rol
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /** Handler que redirige según rol */
    @Component
    static class RoleRedirectHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                            org.springframework.security.core.Authentication auth)
                throws IOException, ServletException {
            var roles = auth.getAuthorities().toString();
            if (roles.contains("ROLE_ADMIN"))      res.sendRedirect("/admin");
            else if (roles.contains("ROLE_COMENSAL")) res.sendRedirect("/comensal");
            else                                     res.sendRedirect("/cliente");
        }
    }
}
