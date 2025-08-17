package com.ipss.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (req, res, auth) -> {
            var roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            String target = "/";
            if (roles.contains("ROLE_ADMIN")) target = "/admin";
            else if (roles.contains("ROLE_CLIENTE")) target = "/cliente";
            else if (roles.contains("ROLE_COMENSAL")) target = "/comensal";
            res.sendRedirect(target);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationSuccessHandler successHandler) throws Exception {
        http
            .cors(c -> {})
            // Si tu API usa cookies/sesión, quita esta línea
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index", "/login", "/css/**", "/js/**", "/images/**", "/public/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/mesas/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/mesas/**").hasAnyRole("ADMIN","CLIENTE")
                .requestMatchers(HttpMethod.PUT, "/api/mesas/**").hasAnyRole("ADMIN","CLIENTE")
                .requestMatchers(HttpMethod.PATCH, "/api/mesas/**").hasAnyRole("ADMIN","CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/mesas/**").hasRole("ADMIN")

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/cliente/**").hasRole("CLIENTE")
                .requestMatchers("/comensal/**").hasRole("COMENSAL")

                .anyRequest().authenticated()
            )

            .formLogin(f -> f
                .loginPage("/login")
                .permitAll()
                .successHandler(successHandler)
            )

            .logout(l -> l
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
