package com.italo.financeiro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name= "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String tipo; //"RECEITA" ou "DESPESA"

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
