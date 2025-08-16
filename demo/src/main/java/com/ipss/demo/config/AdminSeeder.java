package com.ipss.demo.config;

import com.ipss.demo.model.AppUser;
import com.ipss.demo.model.Role;
import com.ipss.demo.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner initAdmin(AppUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            String adminUser = "admin";
            AppUser u = repo.findByUsername(adminUser).orElse(null);
            if (u == null) {
                u = new AppUser();
                u.setUsername(adminUser);
                u.setPassword(encoder.encode("admin")); 
                u.setRole(Role.ADMIN);
                u.setEnabled(true);
                repo.save(u);
                System.out.println("[Seeder] Usuario admin/admin creado");
            } else if (u.getRole() != Role.ADMIN) {
                u.setRole(Role.ADMIN);
                u.setEnabled(true);
                repo.save(u);
                System.out.println("[Seeder] Usuario admin corregido a rol ADMIN");
            }
        };
    }
}
