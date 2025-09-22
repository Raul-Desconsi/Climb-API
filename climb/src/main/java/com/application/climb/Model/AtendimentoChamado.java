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

    @Column(nullable = false)
    private LocalDateTime dataAtendimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsavel_atendimento_id", nullable = false)
    private Funcionario responsavelAtendimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;
}
