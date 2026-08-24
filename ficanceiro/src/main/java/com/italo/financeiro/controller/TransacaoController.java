package com.italo.financeiro.controller;

import com.italo.financeiro.model.Transacao;
import com.italo.financeiro.service.TransacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transacao> listar() {
        return service.listarTodas();
    }
    @PostMapping
    public Transacao criar(@RequestBody Transacao transacao) {
        return service.salvar(transacao);
    }
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
