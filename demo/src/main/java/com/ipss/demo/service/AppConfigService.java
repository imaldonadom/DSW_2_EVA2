package com.ipss.demo.service;

import com.ipss.demo.model.AppConfig;
import com.ipss.demo.repository.AppConfigRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AppConfigService {
    private final AppConfigRepository repo;
    public AppConfigService(AppConfigRepository repo) { this.repo = repo; }

    public AppConfig getOrCreate() {
        return repo.findAll().stream().findFirst()
                .orElseGet(() -> repo.save(new AppConfig(LocalTime.of(12,0), LocalTime.of(23,0))));
    }

    public AppConfig update(LocalTime apertura, LocalTime cierre) {
        AppConfig cfg = getOrCreate();
        cfg.setApertura(apertura);
        cfg.setCierre(cierre);
        return repo.save(cfg);
    }
}

