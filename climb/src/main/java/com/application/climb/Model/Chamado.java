package com.application.climb.Model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chamado")
@Getter
@Setter
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(length = 60, nullable = false)
    private String areaAfetada;

    @Column(length = 255, nullable = false)
    private String descricao;

    @ManyToOne
    private Funcionario responsavelAbertura;

    @ManyToOne
    private Funcionario responsavelResolucao;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Urgencia urgencia;

    public enum Urgencia {
        Baixa,
        Media,
        Alta
    }

    @ManyToOne
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Status status;

    public enum Status {
        Analise,
        Desenvolvimento,
        Devolvido,
        Concluido
    }

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AtendimentoChamado> atendimentosChamado;

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ChamadoSetor> chamadosSetor;
    
}
