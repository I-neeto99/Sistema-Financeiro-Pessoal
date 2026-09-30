package com.italo.financeiro.service;

import com.italo.financeiro.model.Categoria;
import com.italo.financeiro.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public Categoria salvar(Categoria categoria) {
        // id é gerado pelo banco; ignora qualquer id enviado pelo cliente
        categoria.setId(null);
        return repository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }
}
