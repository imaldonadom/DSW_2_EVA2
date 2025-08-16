package com.ipss.demo.service;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository repo;

    public MesaService(MesaRepository repo) {
        this.repo = repo;
    }

    public List<Mesa> listar() {
        return repo.findAll();
    }

    public Mesa buscar(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Mesa crear(Mesa m) {
        return repo.save(m);
    }

    public Mesa actualizar(Integer id, Mesa m) {
        return repo.findById(id)
                .map(actual -> {
                    actual.setNumero(m.getNumero());
                    actual.setCapacidad(m.getCapacidad());
                    actual.setUbicacion(m.getUbicacion());
                    actual.setFumadores(m.getFumadores());
                    return repo.save(actual);
                })
                .orElse(null);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
