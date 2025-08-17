package com.ipss.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InMemoryUsers {

    // Marca este como el UserDetailsService principal si convive con otro (DB)
    @Primary
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails cliente = User.withUsername("cliente")
                .password(encoder.encode("cliente123"))
                .roles("CLIENTE")
                .build();

        UserDetails comensal = User.withUsername("comensal")
                .password(encoder.encode("comensal123"))
                .roles("COMENSAL")
                .build();

        return new InMemoryUserDetailsManager(admin, cliente, comensal);
    }
}
