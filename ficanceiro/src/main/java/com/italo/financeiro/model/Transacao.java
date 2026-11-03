package com.italo.financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor da transação deve ser maior que zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "O tipo é obrigatório (RECEITA ou DESPESA)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @NotNull(message = "A data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
