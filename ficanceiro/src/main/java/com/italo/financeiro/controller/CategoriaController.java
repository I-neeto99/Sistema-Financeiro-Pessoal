package com.italo.financeiro.controller;

import com.italo.financeiro.model.Categoria;
import com.italo.financeiro.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }
    @GetMapping
    public List<Categoria> listar() {
        return service.listarTodas();
    }

    @PostMapping
    public Categoria criar(@RequestBody Categoria categoria) {
        return service.salvar(categoria);
    }
}
