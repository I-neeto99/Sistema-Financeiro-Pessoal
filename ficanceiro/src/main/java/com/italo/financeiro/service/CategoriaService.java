package com.italo.financeiro.service;

import com.italo.financeiro.model.Categoria;
import org.springframework.stereotype.Service;
import com.italo.financeiro.repository.CategoriaRepository;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }
    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }
}
