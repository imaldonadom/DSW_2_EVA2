package com.ipss.demo;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.repository.MesaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  CommandLineRunner seed(MesaRepository repo) {
    return args -> {
      if (repo.count() == 0) {
        Mesa m1 = new Mesa(); m1.setNumero(1); m1.setCapacidad(4); m1.setUbicacion("Interior"); m1.setFumadores(false); repo.save(m1);
        Mesa m2 = new Mesa(); m2.setNumero(2); m2.setCapacidad(6); m2.setUbicacion("Patio");    m2.setFumadores(true);  repo.save(m2);
        Mesa m3 = new Mesa(); m3.setNumero(3); m3.setCapacidad(6); m3.setUbicacion("Terraza"); m3.setFumadores(false); repo.save(m3);
      }
    };
  }
}
