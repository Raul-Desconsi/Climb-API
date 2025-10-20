package com.application.climb.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Table(name = "atendimento_chamado")
@Getter
@Setter
public class AtendimentoChamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String resposta;

    @Column(length = 1, nullable = false)
    private int conclusao_chamado;

    @Column(nullable = false)
    private LocalDateTime data_atendimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_direcionado_id", nullable = false)
    private Setor setorDirecionado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_atendimento_id", nullable = false)
    private Setor setorAtendimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsavel_atendimento_id", nullable = false)
    private Funcionario responsavelAtendimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;



}
