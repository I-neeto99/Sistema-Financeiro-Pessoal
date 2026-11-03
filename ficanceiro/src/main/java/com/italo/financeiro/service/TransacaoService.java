package com.italo.financeiro.service;

import com.italo.financeiro.exception.RecursoNaoEncontradoException;
import com.italo.financeiro.model.Categoria;
import com.italo.financeiro.model.Transacao;
import com.italo.financeiro.repository.CategoriaRepository;
import com.italo.financeiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, CategoriaRepository categoriaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Transacao salvar(Transacao transacao) {
        // id é gerado pelo banco; ignora qualquer id enviado pelo cliente
        transacao.setId(null);

        Categoria categoriaInformada = transacao.getCategoria();
        if (categoriaInformada != null) {
            Long categoriaId = categoriaInformada.getId();
            if (categoriaId == null) {
                throw new IllegalArgumentException("Informe o id da categoria");
            }
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + categoriaId));
            transacao.setCategoria(categoria);
        }
        return transacaoRepository.save(transacao);
    }

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Transação não encontrada: " + id);
        }
        transacaoRepository.deleteById(id);
    }
}
