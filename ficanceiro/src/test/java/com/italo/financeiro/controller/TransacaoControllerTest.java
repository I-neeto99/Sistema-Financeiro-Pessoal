package com.italo.financeiro.controller;

import com.italo.financeiro.exception.GlobalExceptionHandler;
import com.italo.financeiro.exception.RecursoNaoEncontradoException;
import com.italo.financeiro.model.TipoTransacao;
import com.italo.financeiro.model.Transacao;
import com.italo.financeiro.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
@Import(GlobalExceptionHandler.class)
class TransacaoControllerTest {

    @Autowired
    MockMvc mvc;
    @MockitoBean
    TransacaoService service;

    private static final String VALIDO =
            "{\"descricao\":\"Mercado\",\"valor\":10.50,\"tipo\":\"DESPESA\",\"data\":\"2026-01-15\"}";

    @Test
    void criaComSucessoRetorna201() throws Exception {
        when(service.salvar(any())).thenReturn(
                new Transacao(1L, "Mercado", new BigDecimal("10.50"), TipoTransacao.DESPESA, LocalDate.of(2026, 1, 15), null));

        mvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON).content(VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void valorZeroRetorna400() throws Exception {
        mvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDO.replace("10.50", "0")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.valor").exists());
    }

    @Test
    void valorAusenteRetorna400NaoNullPointer() throws Exception {
        mvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"x\",\"tipo\":\"DESPESA\",\"data\":\"2026-01-15\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.valor").exists());
    }

    @Test
    void tipoInvalidoRetorna400() throws Exception {
        mvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDO.replace("DESPESA", "banana")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void descricaoVaziaRetorna400() throws Exception {
        mvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDO.replace("Mercado", "  ")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.descricao").exists());
    }

    @Test
    void categoriaInexistenteRetorna404() throws Exception {
        when(service.salvar(any())).thenThrow(new RecursoNaoEncontradoException("Categoria não encontrada: 999"));
        mvc.perform(post("/transacoes").contentType(MediaType.APPLICATION_JSON).content(VALIDO))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarInexistenteRetorna404() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Transação não encontrada: 7")).when(service).deletar(7L);
        mvc.perform(delete("/transacoes/7")).andExpect(status().isNotFound());
    }

    @Test
    void deletarExistenteRetorna204() throws Exception {
        mvc.perform(delete("/transacoes/7")).andExpect(status().isNoContent());
    }
}
