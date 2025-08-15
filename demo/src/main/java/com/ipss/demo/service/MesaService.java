package com.ipss.demo.service;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {
  private final MesaRepository repo;
  public MesaService(MesaRepository repo) { this.repo = repo; }

  public List<Mesa> listar() { return repo.findAll(); }
  public Mesa crear(Mesa m) { return repo.save(m); }
  public Mesa buscar(Long id) { return repo.findById(id).orElse(null); }
  public Mesa actualizar(Long id, Mesa m) {
    return repo.findById(id).map(db -> {
      db.setNumero(m.getNumero());
      db.setCapacidad(m.getCapacidad());
      db.setUbicacion(m.getUbicacion());
      db.setFumadores(m.getFumadores());
      return repo.save(db);
    }).orElse(null);
  }
  public void eliminar(Long id) { repo.deleteById(id); }
}
