package com.ipss.demo.repository;

import com.ipss.demo.model.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, Long> { }

