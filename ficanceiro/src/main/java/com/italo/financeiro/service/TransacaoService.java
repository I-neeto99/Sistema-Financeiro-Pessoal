package com.italo.financeiro.service;

import com.italo.financeiro.model.Transacao;
import com.italo.financeiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {
    // caixa vazia
    private final TransacaoRepository repository;

    // única vez que preenche
    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;}


    public Transacao salvar(Transacao transacao) {
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero");
        }

        return repository.save(transacao);
    }
    public List<Transacao> listarTodas() {
            return repository.findAll();
        }
    public void deletar(Long id) {
        repository.deleteById(id);
    }
    }


