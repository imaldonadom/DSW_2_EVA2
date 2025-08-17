package com.ipss.demo.service;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository repo;

    public MesaService(MesaRepository repo) { this.repo = repo; }

    public List<Mesa> listar() {
        return repo.findAll(); // JpaRepository -> List<Mesa>
    }

    public Mesa findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + id));
    }

    public Mesa guardar(Mesa mesa) { return repo.save(mesa); }

    public void eliminar(Integer id) { repo.deleteById(id); }

    public boolean existsById(Integer id) { return repo.existsById(id); }
}
