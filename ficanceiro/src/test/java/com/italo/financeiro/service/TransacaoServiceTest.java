package com.italo.financeiro.service;

import com.italo.financeiro.exception.RecursoNaoEncontradoException;
import com.italo.financeiro.model.Categoria;
import com.italo.financeiro.model.TipoTransacao;
import com.italo.financeiro.model.Transacao;
import com.italo.financeiro.repository.CategoriaRepository;
import com.italo.financeiro.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    TransacaoRepository transacaoRepository;
    @Mock
    CategoriaRepository categoriaRepository;
    @InjectMocks
    TransacaoService service;

    private Transacao nova() {
        return new Transacao(null, "Mercado", new BigDecimal("10.00"), TipoTransacao.DESPESA, LocalDate.now(), null);
    }

    @Test
    void salvaSemCategoria() {
        Transacao t = nova();
        when(transacaoRepository.save(t)).thenReturn(t);
        assertSame(t, service.salvar(t));
    }

    @Test
    void salvaComCategoriaExistente() {
        Transacao t = nova();
        t.setCategoria(new Categoria(1L, null));
        Categoria real = new Categoria(1L, "Alimentação");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(real));
        when(transacaoRepository.save(t)).thenReturn(t);

        assertEquals("Alimentação", service.salvar(t).getCategoria().getNome());
    }

    @Test
    void categoriaInexistenteLancaNaoEncontrado() {
        Transacao t = nova();
        t.setCategoria(new Categoria(999L, null));
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.salvar(t));
        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void categoriaSemIdLancaArgumentoInvalido() {
        Transacao t = nova();
        t.setCategoria(new Categoria(null, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(t));
    }

    @Test
    void ignoraIdEnviadoPeloCliente() {
        Transacao t = nova();
        t.setId(42L);
        when(transacaoRepository.save(t)).thenReturn(t);
        service.salvar(t);
        assertNull(t.getId());
    }

    @Test
    void deletarInexistenteLancaNaoEncontrado() {
        when(transacaoRepository.existsById(5L)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(5L));
        verify(transacaoRepository, never()).deleteById(any());
    }

    @Test
    void deletarExistenteRemove() {
        when(transacaoRepository.existsById(5L)).thenReturn(true);
        service.deletar(5L);
        verify(transacaoRepository).deleteById(5L);
    }
}
